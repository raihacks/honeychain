package com.honeychain.dto;

import java.util.List;

/** Full audit view of a single batch for /admin/batches/{id}. */
public class AdminBatchDetail {
    private boolean found;
    private Long batchId;
    private String batchCode;
    private String beekeeperName;
    private String hiveCode;
    private String status;
    private Double quantityKg;
    private boolean chainValid;
    private List<String> issues;
    private List<LedgerRecordDetail> records;

    public boolean isFound() { return found; }
    public void setFound(boolean found) { this.found = found; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    public String getBeekeeperName() { return beekeeperName; }
    public void setBeekeeperName(String beekeeperName) { this.beekeeperName = beekeeperName; }
    public String getHiveCode() { return hiveCode; }
    public void setHiveCode(String hiveCode) { this.hiveCode = hiveCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
    public boolean isChainValid() { return chainValid; }
    public void setChainValid(boolean chainValid) { this.chainValid = chainValid; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public List<LedgerRecordDetail> getRecords() { return records; }
    public void setRecords(List<LedgerRecordDetail> records) { this.records = records; }
}
