package com.honeychain.dto;

/** Full (non-truncated) view of one ledger record, for the admin audit drill-down. */
public class LedgerRecordDetail {
    private int sequenceNo;
    private String status;
    private String createdAt;
    private String previousHash;
    private String currentHash;
    private String payloadJson;

    public LedgerRecordDetail(int sequenceNo, String status, String createdAt,
                               String previousHash, String currentHash, String payloadJson) {
        this.sequenceNo = sequenceNo;
        this.status = status;
        this.createdAt = createdAt;
        this.previousHash = previousHash;
        this.currentHash = currentHash;
        this.payloadJson = payloadJson;
    }

    public int getSequenceNo() { return sequenceNo; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getPreviousHash() { return previousHash; }
    public String getCurrentHash() { return currentHash; }
    public String getPayloadJson() { return payloadJson; }
}
