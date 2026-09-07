package com.honeychain.dto;

import java.util.List;

/**
 * Everything the consumer-facing /verify/{batchId} page needs, assembled
 * from Batch + Hive + Beekeeper + SensorReading + the ledger chain.
 */
public class ConsumerVerificationView {

    private boolean found;         // false -> batch code doesn't exist at all
    private Long batchId;
    private String batchCode;
    private String status;
    private Double quantityKg;

    private String beekeeperName;
    private String beekeeperVillage;

    private String hiveCode;
    private String hiveLocation;   // formatted "lat, lon" or "Location not recorded"

    private String harvestDate;    // from the HARVESTED ledger record, or "Not yet harvested"

    private String sensorStatus;   // Healthy / Needs Attention / No sensor data yet
    private Double lastTemperatureC;
    private Double lastHumidityPct;
    private Double lastWeightKg;
    private String lastReadingAt;

    private boolean chainValid;
    private List<LedgerTrailEntry> ledgerTrail;
    private List<String> chainIssues;

    public boolean isFound() { return found; }
    public void setFound(boolean found) { this.found = found; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
    public String getBeekeeperName() { return beekeeperName; }
    public void setBeekeeperName(String beekeeperName) { this.beekeeperName = beekeeperName; }
    public String getBeekeeperVillage() { return beekeeperVillage; }
    public void setBeekeeperVillage(String beekeeperVillage) { this.beekeeperVillage = beekeeperVillage; }
    public String getHiveCode() { return hiveCode; }
    public void setHiveCode(String hiveCode) { this.hiveCode = hiveCode; }
    public String getHiveLocation() { return hiveLocation; }
    public void setHiveLocation(String hiveLocation) { this.hiveLocation = hiveLocation; }
    public String getHarvestDate() { return harvestDate; }
    public void setHarvestDate(String harvestDate) { this.harvestDate = harvestDate; }
    public String getSensorStatus() { return sensorStatus; }
    public void setSensorStatus(String sensorStatus) { this.sensorStatus = sensorStatus; }
    public Double getLastTemperatureC() { return lastTemperatureC; }
    public void setLastTemperatureC(Double lastTemperatureC) { this.lastTemperatureC = lastTemperatureC; }
    public Double getLastHumidityPct() { return lastHumidityPct; }
    public void setLastHumidityPct(Double lastHumidityPct) { this.lastHumidityPct = lastHumidityPct; }
    public Double getLastWeightKg() { return lastWeightKg; }
    public void setLastWeightKg(Double lastWeightKg) { this.lastWeightKg = lastWeightKg; }
    public String getLastReadingAt() { return lastReadingAt; }
    public void setLastReadingAt(String lastReadingAt) { this.lastReadingAt = lastReadingAt; }
    public boolean isChainValid() { return chainValid; }
    public void setChainValid(boolean chainValid) { this.chainValid = chainValid; }
    public List<LedgerTrailEntry> getLedgerTrail() { return ledgerTrail; }
    public void setLedgerTrail(List<LedgerTrailEntry> ledgerTrail) { this.ledgerTrail = ledgerTrail; }
    public List<String> getChainIssues() { return chainIssues; }
    public void setChainIssues(List<String> chainIssues) { this.chainIssues = chainIssues; }
}
