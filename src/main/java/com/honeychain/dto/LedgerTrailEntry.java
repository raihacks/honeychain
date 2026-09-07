package com.honeychain.dto;

/**
 * One row of the public-facing hash trail shown on the consumer
 * verification page - deliberately smaller than LedgerRecord (no internal
 * payload dump), just enough for a consumer to see the chain is real.
 */
public class LedgerTrailEntry {
    private int sequenceNo;
    private String status;
    private String timestamp;
    private String hash;

    public LedgerTrailEntry(int sequenceNo, String status, String timestamp, String hash) {
        this.sequenceNo = sequenceNo;
        this.status = status;
        this.timestamp = timestamp;
        this.hash = hash;
    }

    public int getSequenceNo() { return sequenceNo; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
    public String getHash() { return hash; }

    /** First 12 + last 6 hex chars, for compact display: e.g. "3f9a2c1d8e7b...4a1f9c" */
    public String getShortHash() {
        if (hash == null || hash.length() < 20) return hash;
        return hash.substring(0, 12) + "…" + hash.substring(hash.length() - 6);
    }
}
