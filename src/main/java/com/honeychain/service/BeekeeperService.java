package com.honeychain.service;

import com.honeychain.dto.RegisterBeekeeperRequest;
import com.honeychain.entity.Beekeeper;
import com.honeychain.repository.BeekeeperRepository;
import org.springframework.stereotype.Service;

@Service
public class BeekeeperService {

    private final BeekeeperRepository beekeeperRepository;

    public BeekeeperService(BeekeeperRepository beekeeperRepository) {
        this.beekeeperRepository = beekeeperRepository;
    }

    public Beekeeper register(RegisterBeekeeperRequest req) {
        if (beekeeperRepository.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("A beekeeper with this phone already exists: " + req.getPhone());
        }
        Beekeeper beekeeper = new Beekeeper();
        beekeeper.setName(req.getName());
        beekeeper.setPhone(req.getPhone());
        beekeeper.setVillage(req.getVillage());
        beekeeper.setCluster(req.getCluster());
        return beekeeperRepository.save(beekeeper);
    }
}
