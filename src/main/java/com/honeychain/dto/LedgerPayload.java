package com.honeychain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.honeychain.entity.BatchStatus;

/**
 * The exact fields that get serialized to JSON and fed into the hash
 * function for each ledger record. @JsonPropertyOrder pins the field
 * order so serialization is deterministic; the resulting JSON string is
 * itself stored on the LedgerRecord (payloadJson), so verification never
 * has to re-serialize this class - it just re-hashes the stored string.
 */
@JsonPropertyOrder({ "batchId", "batchCode", "hiveCode", "status", "quantityKg", "sequenceNo", "timestamp" })
public class LedgerPayload {

    private Long batchId;
    private String batchCode;
    private String hiveCode;
    private BatchStatus status;
    private Double quantityKg;
    private Integer sequenceNo;
    private String timestamp; // ISO-8601 string, fixed at record-creation time

    public LedgerPayload() { }

    public LedgerPayload(Long batchId, String batchCode, String hiveCode, BatchStatus status,
                          Double quantityKg, Integer sequenceNo, String timestamp) {
        this.batchId = batchId;
        this.batchCode = batchCode;
        this.hiveCode = hiveCode;
        this.status = status;
        this.quantityKg = quantityKg;
        this.sequenceNo = sequenceNo;
        this.timestamp = timestamp;
    }

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    public String getHiveCode() { return hiveCode; }
    public void setHiveCode(String hiveCode) { this.hiveCode = hiveCode; }
    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
