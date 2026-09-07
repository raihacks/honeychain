package com.honeychain.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterBeekeeperRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String village;
    private String cluster;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getCluster() { return cluster; }
    public void setCluster(String cluster) { this.cluster = cluster; }
}
