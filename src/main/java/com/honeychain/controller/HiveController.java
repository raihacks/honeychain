package com.honeychain.controller;

import com.honeychain.dto.RegisterHiveRequest;
import com.honeychain.entity.Hive;
import com.honeychain.service.HiveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hives")
public class HiveController {

    private final HiveService hiveService;

    public HiveController(HiveService hiveService) {
        this.hiveService = hiveService;
    }

    @PostMapping
    public ResponseEntity<Hive> register(@Valid @RequestBody RegisterHiveRequest req) {
        Hive saved = hiveService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
