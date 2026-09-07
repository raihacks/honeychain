package com.honeychain.dto;

/** One row in the admin overview's batch table - status + chain-integrity summary. */
public class AdminBatchRow {
    private Long batchId;
    private String batchCode;
    private String beekeeperName;
    private String hiveCode;
    private String status;
    private boolean chainValid;
    private int issuesCount;
    private int recordsChecked;

    public AdminBatchRow(Long batchId, String batchCode, String beekeeperName, String hiveCode,
                          String status, boolean chainValid, int issuesCount, int recordsChecked) {
        this.batchId = batchId;
        this.batchCode = batchCode;
        this.beekeeperName = beekeeperName;
        this.hiveCode = hiveCode;
        this.status = status;
        this.chainValid = chainValid;
        this.issuesCount = issuesCount;
        this.recordsChecked = recordsChecked;
    }

    public Long getBatchId() { return batchId; }
    public String getBatchCode() { return batchCode; }
    public String getBeekeeperName() { return beekeeperName; }
    public String getHiveCode() { return hiveCode; }
    public String getStatus() { return status; }
    public boolean isChainValid() { return chainValid; }
    public int getIssuesCount() { return issuesCount; }
    public int getRecordsChecked() { return recordsChecked; }
}
