package com.honeychain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "batch")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Public-facing code, e.g. encoded into the consumer QR label
    @Column(unique = true, nullable = false)
    private String batchCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status = BatchStatus.CREATED;

    private Double quantityKg;

    // Denormalized pointer to the most recent ledger record's hash,
    // so we don't have to scan the ledger to know "where the chain is".
    @Column(length = 64)
    private String latestHash;

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<LedgerRecord> ledgerRecords = new ArrayList<>();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }

    public Hive getHive() { return hive; }
    public void setHive(Hive hive) { this.hive = hive; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }

    public String getLatestHash() { return latestHash; }
    public void setLatestHash(String latestHash) { this.latestHash = latestHash; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<LedgerRecord> getLedgerRecords() { return ledgerRecords; }
    public void setLedgerRecords(List<LedgerRecord> ledgerRecords) { this.ledgerRecords = ledgerRecords; }
}
