package com.honeychain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "beekeeper")
public class Beekeeper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phone;

    private String village;

    private String cluster; // links to a KVIC cluster / cooperative

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "beekeeper", cascade = CascadeType.ALL)
    private List<Hive> hives = new ArrayList<>();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getCluster() { return cluster; }
    public void setCluster(String cluster) { this.cluster = cluster; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<Hive> getHives() { return hives; }
    public void setHives(List<Hive> hives) { this.hives = hives; }
}
