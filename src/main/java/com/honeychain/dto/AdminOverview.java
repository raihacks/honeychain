package com.honeychain.dto;

import java.util.List;
import java.util.Map;

/** Everything the /admin landing page needs: system-wide counts + per-batch integrity status. */
public class AdminOverview {
    private long totalBeekeepers;
    private long totalHives;
    private long totalBatches;
    private long totalSensorReadings;
    private Map<String, Long> statusCounts;
    private List<AdminBatchRow> batches;
    private long invalidBatchCount;

    public long getTotalBeekeepers() { return totalBeekeepers; }
    public void setTotalBeekeepers(long totalBeekeepers) { this.totalBeekeepers = totalBeekeepers; }
    public long getTotalHives() { return totalHives; }
    public void setTotalHives(long totalHives) { this.totalHives = totalHives; }
    public long getTotalBatches() { return totalBatches; }
    public void setTotalBatches(long totalBatches) { this.totalBatches = totalBatches; }
    public long getTotalSensorReadings() { return totalSensorReadings; }
    public void setTotalSensorReadings(long totalSensorReadings) { this.totalSensorReadings = totalSensorReadings; }
    public Map<String, Long> getStatusCounts() { return statusCounts; }
    public void setStatusCounts(Map<String, Long> statusCounts) { this.statusCounts = statusCounts; }
    public List<AdminBatchRow> getBatches() { return batches; }
    public void setBatches(List<AdminBatchRow> batches) { this.batches = batches; }
    public long getInvalidBatchCount() { return invalidBatchCount; }
    public void setInvalidBatchCount(long invalidBatchCount) { this.invalidBatchCount = invalidBatchCount; }
}
