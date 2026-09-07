package com.honeychain.config;

import com.honeychain.dto.*;
import com.honeychain.entity.Batch;
import com.honeychain.entity.BatchStatus;
import com.honeychain.entity.Beekeeper;
import com.honeychain.entity.Hive;
import com.honeychain.repository.BatchRepository;
import com.honeychain.service.BatchService;
import com.honeychain.service.BeekeeperService;
import com.honeychain.service.HiveService;
import com.honeychain.service.SensorReadingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BatchRepository batchRepository;
    private final BeekeeperService beekeeperService;
    private final HiveService hiveService;
    private final BatchService batchService;
    private final SensorReadingService sensorReadingService;

    public DataSeeder(BatchRepository batchRepository,
                       BeekeeperService beekeeperService,
                       HiveService hiveService,
                       BatchService batchService,
                       SensorReadingService sensorReadingService) {
        this.batchRepository = batchRepository;
        this.beekeeperService = beekeeperService;
        this.hiveService = hiveService;
        this.batchService = batchService;
        this.sensorReadingService = sensorReadingService;
    }

    @Override
    public void run(String... args) {
        if (batchRepository.count() > 0) {
            return;
        }

        Beekeeper ramesh = beekeeperService.register(beekeeper(
                "Ramesh Kumar", "9876543210", "Sonipat", "KVIC-North-1"));

        Hive rameshHive1 = hiveService.register(hive("HIVE-001", ramesh.getId(), 28.9931, 77.0151));
        Hive rameshHive2 = hiveService.register(hive("HIVE-002", ramesh.getId(), 28.9945, 77.0163));

        sensorReadingService.add(reading(rameshHive1.getId(), 34.2, 55.0, 41.3, 0.8));
        sensorReadingService.add(reading(rameshHive1.getId(), 33.8, 57.5, 42.1, 0.7));
        sensorReadingService.add(reading(rameshHive2.getId(), 35.6, 62.0, 38.4, 1.4)); 

        Batch batch1 = batchService.createBatch(createBatch("BATCH-2026-001", rameshHive1.getId(), 12.5));
        batchService.transitionStatus(batch1.getId(), BatchStatus.HARVESTED);
        batchService.transitionStatus(batch1.getId(), BatchStatus.QUALITY_CHECKED);
        batchService.transitionStatus(batch1.getId(), BatchStatus.PACKAGED);

        Batch batch2 = batchService.createBatch(createBatch("BATCH-2026-002", rameshHive2.getId(), 9.0));
        batchService.transitionStatus(batch2.getId(), BatchStatus.HARVESTED);
        batchService.transitionStatus(batch2.getId(), BatchStatus.QUALITY_CHECKED);

        Beekeeper sunita = beekeeperService.register(beekeeper(
                "Sunita Devi", "9123456780", "Panipat", "KVIC-North-1"));

        Hive sunitaHive = hiveService.register(hive("HIVE-101", sunita.getId(), 29.3909, 76.9635));
        sensorReadingService.add(reading(sunitaHive.getId(), 33.1, 51.0, 22.7, 0.5));

        batchService.createBatch(createBatch("BATCH-2026-101", sunitaHive.getId(), 6.2));

        System.out.println("[DataSeeder] Seeded 2 beekeepers, 3 hives, 3 batches (PACKAGED / QUALITY_CHECKED / CREATED), "
                + "4 sensor readings. Try: GET /api/batches/" + batch1.getId() + "/verify or /verify/" + batch1.getId());
    }

    private RegisterBeekeeperRequest beekeeper(String name, String phone, String village, String cluster) {
        RegisterBeekeeperRequest req = new RegisterBeekeeperRequest();
        req.setName(name);
        req.setPhone(phone);
        req.setVillage(village);
        req.setCluster(cluster);
        return req;
    }

    private RegisterHiveRequest hive(String hiveCode, Long beekeeperId, double lat, double lon) {
        RegisterHiveRequest req = new RegisterHiveRequest();
        req.setHiveCode(hiveCode);
        req.setBeekeeperId(beekeeperId);
        req.setLatitude(lat);
        req.setLongitude(lon);
        return req;
    }

    private AddSensorReadingRequest reading(Long hiveId, double tempC, double humidityPct, double weightKg, double soundIndex) {
        AddSensorReadingRequest req = new AddSensorReadingRequest();
        req.setHiveId(hiveId);
        req.setTemperatureC(tempC);
        req.setHumidityPct(humidityPct);
        req.setWeightKg(weightKg);
        req.setSoundIndex(soundIndex);
        return req;
    }

    private CreateBatchRequest createBatch(String batchCode, Long hiveId, double quantityKg) {
        CreateBatchRequest req = new CreateBatchRequest();
        req.setBatchCode(batchCode);
        req.setHiveId(hiveId);
        req.setQuantityKg(quantityKg);
        return req;
    }
}
