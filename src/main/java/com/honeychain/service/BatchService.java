package com.honeychain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeychain.dto.ChainVerificationResult;
import com.honeychain.dto.CreateBatchRequest;
import com.honeychain.dto.LedgerPayload;
import com.honeychain.entity.*;
import com.honeychain.repository.BatchRepository;
import com.honeychain.repository.HiveRepository;
import com.honeychain.repository.LedgerRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BatchService {

    // Defines the only legal forward transitions. CREATED -> HARVESTED ->
    // QUALITY_CHECKED -> PACKAGED. No skipping stages, no going backwards.
    private static final List<BatchStatus> ORDER = List.of(
            BatchStatus.CREATED,
            BatchStatus.HARVESTED,
            BatchStatus.QUALITY_CHECKED,
            BatchStatus.PACKAGED
    );

    private final BatchRepository batchRepository;
    private final HiveRepository hiveRepository;
    private final LedgerRecordRepository ledgerRecordRepository;
    private final HashingService hashingService;
    private final ObjectMapper objectMapper;

    public BatchService(BatchRepository batchRepository,
                         HiveRepository hiveRepository,
                         LedgerRecordRepository ledgerRecordRepository,
                         HashingService hashingService,
                         ObjectMapper objectMapper) {
        this.batchRepository = batchRepository;
        this.hiveRepository = hiveRepository;
        this.ledgerRecordRepository = ledgerRecordRepository;
        this.hashingService = hashingService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Batch createBatch(CreateBatchRequest req) {
        if (batchRepository.existsByBatchCode(req.getBatchCode())) {
            throw new IllegalArgumentException("batchCode already exists: " + req.getBatchCode());
        }
        Hive hive = hiveRepository.findById(req.getHiveId())
                .orElseThrow(() -> new NotFoundException("Hive not found: " + req.getHiveId()));

        Batch batch = new Batch();
        batch.setBatchCode(req.getBatchCode());
        batch.setHive(hive);
        batch.setQuantityKg(req.getQuantityKg());
        batch.setStatus(BatchStatus.CREATED);
        batch = batchRepository.save(batch);

        // Genesis link in this batch's chain, recording the CREATED state.
        appendLedgerRecord(batch, BatchStatus.CREATED);

        return batch;
    }

    /**
     * Advances a batch to newStatus, appending a new hash-chained ledger
     * record. Only the next status in ORDER is accepted - no skipping and
     * no moving backwards, so the chain always reflects a real, in-order
     * production history.
     */
    @Transactional
    public Batch transitionStatus(Long batchId, BatchStatus newStatus) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("Batch not found: " + batchId));

        int currentIdx = ORDER.indexOf(batch.getStatus());
        int newIdx = ORDER.indexOf(newStatus);

        if (newIdx != currentIdx + 1) {
            throw new IllegalBatchTransitionException(
                    "Cannot move batch " + batchId + " from " + batch.getStatus() +
                            " to " + newStatus + ". Expected next status: " +
                            (currentIdx + 1 < ORDER.size() ? ORDER.get(currentIdx + 1) : "none (already final)"));
        }

        batch.setStatus(newStatus);
        batch.setUpdatedAt(Instant.now());
        batch = batchRepository.save(batch);

        appendLedgerRecord(batch, newStatus);

        return batch;
    }

    /**
     * Builds the payload for the given status, computes
     * SHA-256(previousHash + json(payload)), and persists the new
     * LedgerRecord. previousHash is the batch's latestHash, or the fixed
     * genesis hash if this is the first record in the chain.
     */
    private LedgerRecord appendLedgerRecord(Batch batch, BatchStatus status) {
        int nextSeq = ledgerRecordRepository.findTopByBatchIdOrderBySequenceNoDesc(batch.getId())
                .map(r -> r.getSequenceNo() + 1)
                .orElse(1);

        String previousHash = batch.getLatestHash() != null
                ? batch.getLatestHash()
                : HashingService.GENESIS_HASH;

        LedgerPayload payload = new LedgerPayload(
                batch.getId(),
                batch.getBatchCode(),
                batch.getHive().getHiveCode(),
                status,
                batch.getQuantityKg(),
                nextSeq,
                Instant.now().toString()
        );

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize ledger payload", e);
        }

        String currentHash = hashingService.sha256(previousHash, payloadJson);

        LedgerRecord record = new LedgerRecord();
        record.setBatch(batch);
        record.setSequenceNo(nextSeq);
        record.setStatus(status);
        record.setPayloadJson(payloadJson);
        record.setPreviousHash(previousHash);
        record.setCurrentHash(currentHash);
        record = ledgerRecordRepository.save(record);

        batch.setLatestHash(currentHash);
        batchRepository.save(batch);

        return record;
    }

    /**
     * Recomputes every hash in a batch's chain from its stored payload and
     * confirms:
     *   1) each record's previousHash matches the prior record's currentHash
     *      (or GENESIS_HASH for the first record)
     *   2) SHA-256(previousHash + payloadJson) still equals the stored
     *      currentHash (i.e. the payload/hash pair hasn't been tampered with)
     *   3) sequence numbers are contiguous starting at 1
     */
    @Transactional(readOnly = true)
    public ChainVerificationResult verifyChain(Long batchId) {
        if (!batchRepository.existsById(batchId)) {
            throw new NotFoundException("Batch not found: " + batchId);
        }

        List<LedgerRecord> chain = ledgerRecordRepository.findByBatchIdOrderBySequenceNoAsc(batchId);

        ChainVerificationResult result = new ChainVerificationResult();
        result.setBatchId(batchId);
        result.setRecordsChecked(chain.size());

        String expectedPreviousHash = HashingService.GENESIS_HASH;
        int expectedSeq = 1;
        boolean valid = true;

        for (LedgerRecord record : chain) {
            if (!record.getSequenceNo().equals(expectedSeq)) {
                result.addIssue("Sequence gap at position " + expectedSeq +
                        " (found sequenceNo=" + record.getSequenceNo() + ")");
                valid = false;
            }

            if (!record.getPreviousHash().equals(expectedPreviousHash)) {
                result.addIssue("Broken link at sequenceNo=" + record.getSequenceNo() +
                        ": previousHash does not match prior record's currentHash");
                valid = false;
            }

            String recomputedHash = hashingService.sha256(record.getPreviousHash(), record.getPayloadJson());
            if (!recomputedHash.equals(record.getCurrentHash())) {
                result.addIssue("Tampered record at sequenceNo=" + record.getSequenceNo() +
                        ": stored hash does not match recomputed hash");
                valid = false;
            }

            expectedPreviousHash = record.getCurrentHash();
            expectedSeq++;
        }

        result.setValid(valid);
        return result;
    }
}
