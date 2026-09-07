package com.honeychain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hive")
public class Hive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Human-facing code printed on the physical hive / used in QR chains
    @Column(unique = true, nullable = false)
    private String hiveCode;

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beekeeper_id", nullable = false)
    private Beekeeper beekeeper;

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "hive", cascade = CascadeType.ALL)
    private List<SensorReading> sensorReadings = new ArrayList<>();

    @OneToMany(mappedBy = "hive", cascade = CascadeType.ALL)
    private List<Batch> batches = new ArrayList<>();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHiveCode() { return hiveCode; }
    public void setHiveCode(String hiveCode) { this.hiveCode = hiveCode; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Beekeeper getBeekeeper() { return beekeeper; }
    public void setBeekeeper(Beekeeper beekeeper) { this.beekeeper = beekeeper; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<SensorReading> getSensorReadings() { return sensorReadings; }
    public void setSensorReadings(List<SensorReading> sensorReadings) { this.sensorReadings = sensorReadings; }

    public List<Batch> getBatches() { return batches; }
    public void setBatches(List<Batch> batches) { this.batches = batches; }
}
