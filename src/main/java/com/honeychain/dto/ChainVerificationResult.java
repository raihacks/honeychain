package com.honeychain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Response payload for the chain-integrity verification endpoint.
 */
public class ChainVerificationResult {
    private boolean valid;
    private Long batchId;
    private int recordsChecked;
    private List<String> issues = new ArrayList<>();

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public int getRecordsChecked() { return recordsChecked; }
    public void setRecordsChecked(int recordsChecked) { this.recordsChecked = recordsChecked; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public void addIssue(String issue) { this.issues.add(issue); }
}
