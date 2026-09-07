package com.honeychain.service;

import com.honeychain.dto.RegisterHiveRequest;
import com.honeychain.entity.Beekeeper;
import com.honeychain.entity.Hive;
import com.honeychain.repository.BeekeeperRepository;
import com.honeychain.repository.HiveRepository;
import org.springframework.stereotype.Service;

@Service
public class HiveService {

    private final HiveRepository hiveRepository;
    private final BeekeeperRepository beekeeperRepository;

    public HiveService(HiveRepository hiveRepository, BeekeeperRepository beekeeperRepository) {
        this.hiveRepository = hiveRepository;
        this.beekeeperRepository = beekeeperRepository;
    }

    public Hive register(RegisterHiveRequest req) {
        if (hiveRepository.existsByHiveCode(req.getHiveCode())) {
            throw new IllegalArgumentException("A hive with this code already exists: " + req.getHiveCode());
        }
        Beekeeper beekeeper = beekeeperRepository.findById(req.getBeekeeperId())
                .orElseThrow(() -> new NotFoundException("Beekeeper not found: " + req.getBeekeeperId()));

        Hive hive = new Hive();
        hive.setHiveCode(req.getHiveCode());
        hive.setBeekeeper(beekeeper);
        hive.setLatitude(req.getLatitude());
        hive.setLongitude(req.getLongitude());
        return hiveRepository.save(hive);
    }
}
