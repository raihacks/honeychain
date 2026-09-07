package com.honeychain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterHiveRequest {
    @NotBlank
    private String hiveCode;
    @NotNull
    private Long beekeeperId;
    private Double latitude;
    private Double longitude;

    public String getHiveCode() { return hiveCode; }
    public void setHiveCode(String hiveCode) { this.hiveCode = hiveCode; }
    public Long getBeekeeperId() { return beekeeperId; }
    public void setBeekeeperId(Long beekeeperId) { this.beekeeperId = beekeeperId; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
