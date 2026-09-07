package com.honeychain.service;

import com.honeychain.dto.*;
import com.honeychain.entity.Batch;
import com.honeychain.entity.LedgerRecord;
import com.honeychain.repository.BatchRepository;
import com.honeychain.repository.BeekeeperRepository;
import com.honeychain.repository.HiveRepository;
import com.honeychain.repository.LedgerRecordRepository;
import com.honeychain.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Read-only, system-wide view for cooperative/KVIC administrators:
 * counts across the whole system, and a hash-chain integrity check run
 * against every batch (not just one, like the consumer /verify page).
 */
@Service
public class AdminService {

    private final BeekeeperRepository beekeeperRepository;
    private final HiveRepository hiveRepository;
    private final BatchRepository batchRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final LedgerRecordRepository ledgerRecordRepository;
    private final BatchService batchService;

    public AdminService(BeekeeperRepository beekeeperRepository,
                         HiveRepository hiveRepository,
                         BatchRepository batchRepository,
                         SensorReadingRepository sensorReadingRepository,
                         LedgerRecordRepository ledgerRecordRepository,
                         BatchService batchService) {
        this.beekeeperRepository = beekeeperRepository;
        this.hiveRepository = hiveRepository;
        this.batchRepository = batchRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.ledgerRecordRepository = ledgerRecordRepository;
        this.batchService = batchService;
    }

    @Transactional(readOnly = true)
    public AdminOverview buildOverview() {
        List<Batch> allBatches = batchRepository.findAll();

        List<AdminBatchRow> rows = allBatches.stream()
                .map(batch -> {
                    ChainVerificationResult result = batchService.verifyChain(batch.getId());
                    return new AdminBatchRow(
                            batch.getId(),
                            batch.getBatchCode(),
                            batch.getHive().getBeekeeper().getName(),
                            batch.getHive().getHiveCode(),
                            batch.getStatus().name(),
                            result.isValid(),
                            result.getIssues().size(),
                            result.getRecordsChecked()
                    );
                })
                .collect(Collectors.toList());

        AdminOverview overview = new AdminOverview();
        overview.setTotalBeekeepers(beekeeperRepository.count());
        overview.setTotalHives(hiveRepository.count());
        overview.setTotalBatches(allBatches.size());
        overview.setTotalSensorReadings(sensorReadingRepository.count());
        overview.setStatusCounts(
                allBatches.stream()
                        .collect(Collectors.groupingBy(b -> b.getStatus().name(), Collectors.counting()))
        );
        overview.setBatches(rows);
        overview.setInvalidBatchCount(rows.stream().filter(r -> !r.isChainValid()).count());

        return overview;
    }

    @Transactional(readOnly = true)
    public AdminBatchDetail buildBatchDetail(Long batchId) {
        AdminBatchDetail detail = new AdminBatchDetail();

        Optional<Batch> maybeBatch = batchRepository.findById(batchId);
        if (maybeBatch.isEmpty()) {
            detail.setFound(false);
            return detail;
        }
        Batch batch = maybeBatch.get();
        detail.setFound(true);
        detail.setBatchId(batch.getId());
        detail.setBatchCode(batch.getBatchCode());
        detail.setBeekeeperName(batch.getHive().getBeekeeper().getName());
        detail.setHiveCode(batch.getHive().getHiveCode());
        detail.setStatus(batch.getStatus().name());
        detail.setQuantityKg(batch.getQuantityKg());

        ChainVerificationResult result = batchService.verifyChain(batchId);
        detail.setChainValid(result.isValid());
        detail.setIssues(result.getIssues());

        List<LedgerRecord> records = ledgerRecordRepository.findByBatchIdOrderBySequenceNoAsc(batchId);
        detail.setRecords(
                records.stream()
                        .map(r -> new LedgerRecordDetail(
                                r.getSequenceNo(),
                                r.getStatus().name(),
                                r.getCreatedAt().toString(),
                                r.getPreviousHash(),
                                r.getCurrentHash(),
                                r.getPayloadJson()))
                        .collect(Collectors.toList())
        );

        return detail;
    }
}
