package com.honeychain.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * One link in the hash chain for a single Batch. Each record captures the
 * batch's state at a state-transition event, plus the hash of the
 * previous record, plus its own hash:
 *
 *   currentHash = SHA-256(previousHash + json(payload))
 *
 * The chain for a batch is the ordered set of LedgerRecords with the same
 * batchId, ordered by sequenceNo. sequenceNo 1's previousHash is a fixed
 * genesis constant ("0"*64) since there is no prior record.
 */
@Entity
@Table(name = "ledger_record", indexes = {
        @Index(name = "idx_ledger_batch_seq", columnList = "batch_id,sequenceNo", unique = true)
})
public class LedgerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    // 1-based position of this record within its batch's chain
    @Column(nullable = false)
    private Integer sequenceNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status;

    // Exact JSON string that was hashed (the "current_record" payload).
    // Stored verbatim so verification can recompute the hash byte-for-byte.
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false, length = 64)
    private String previousHash;

    @Column(nullable = false, length = 64)
    private String currentHash;

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }

    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }

    public String getCurrentHash() { return currentHash; }
    public void setCurrentHash(String currentHash) { this.currentHash = currentHash; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
