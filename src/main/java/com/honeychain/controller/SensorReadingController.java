package com.honeychain.controller;

import com.honeychain.dto.AddSensorReadingRequest;
import com.honeychain.entity.SensorReading;
import com.honeychain.service.SensorReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor-readings")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @PostMapping
    public ResponseEntity<SensorReading> add(@Valid @RequestBody AddSensorReadingRequest req) {
        SensorReading saved = sensorReadingService.add(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
