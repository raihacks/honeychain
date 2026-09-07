package com.honeychain.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sensor_reading")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive;

    private Double temperatureC;
    private Double humidityPct;
    private Double weightKg;

    // Optional: sound/vibration index used later for AI disease/health detection
    private Double soundIndex;

    @Column(updatable = false)
    private Instant recordedAt = Instant.now();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Hive getHive() { return hive; }
    public void setHive(Hive hive) { this.hive = hive; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public Double getHumidityPct() { return humidityPct; }
    public void setHumidityPct(Double humidityPct) { this.humidityPct = humidityPct; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public Double getSoundIndex() { return soundIndex; }
    public void setSoundIndex(Double soundIndex) { this.soundIndex = soundIndex; }

    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
