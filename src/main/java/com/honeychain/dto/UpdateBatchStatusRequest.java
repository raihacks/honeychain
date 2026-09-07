package com.honeychain.dto;

import com.honeychain.entity.BatchStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateBatchStatusRequest {
    @NotNull
    private BatchStatus newStatus;

    public BatchStatus getNewStatus() { return newStatus; }
    public void setNewStatus(BatchStatus newStatus) { this.newStatus = newStatus; }
}
