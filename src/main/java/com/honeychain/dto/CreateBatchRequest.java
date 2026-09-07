package com.honeychain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateBatchRequest {
    @NotBlank
    private String batchCode;
    @NotNull
    private Long hiveId;
    private Double quantityKg;

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    public Long getHiveId() { return hiveId; }
    public void setHiveId(Long hiveId) { this.hiveId = hiveId; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
}
