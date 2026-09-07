package com.honeychain.service;

import com.honeychain.dto.AddSensorReadingRequest;
import com.honeychain.entity.Hive;
import com.honeychain.entity.SensorReading;
import com.honeychain.repository.HiveRepository;
import com.honeychain.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final HiveRepository hiveRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository, HiveRepository hiveRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.hiveRepository = hiveRepository;
    }

    public SensorReading add(AddSensorReadingRequest req) {
        Hive hive = hiveRepository.findById(req.getHiveId())
                .orElseThrow(() -> new NotFoundException("Hive not found: " + req.getHiveId()));

        SensorReading reading = new SensorReading();
        reading.setHive(hive);
        reading.setTemperatureC(req.getTemperatureC());
        reading.setHumidityPct(req.getHumidityPct());
        reading.setWeightKg(req.getWeightKg());
        reading.setSoundIndex(req.getSoundIndex());
        return sensorReadingRepository.save(reading);
    }
}
