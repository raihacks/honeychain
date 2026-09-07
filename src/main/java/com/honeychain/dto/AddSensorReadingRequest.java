package com.honeychain.dto;

import jakarta.validation.constraints.NotNull;

public class AddSensorReadingRequest {
    @NotNull
    private Long hiveId;
    private Double temperatureC;
    private Double humidityPct;
    private Double weightKg;
    private Double soundIndex;

    public Long getHiveId() { return hiveId; }
    public void setHiveId(Long hiveId) { this.hiveId = hiveId; }
    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }
    public Double getHumidityPct() { return humidityPct; }
    public void setHumidityPct(Double humidityPct) { this.humidityPct = humidityPct; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public Double getSoundIndex() { return soundIndex; }
    public void setSoundIndex(Double soundIndex) { this.soundIndex = soundIndex; }
}
