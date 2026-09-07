package com.honeychain.controller;

import com.honeychain.dto.RegisterBeekeeperRequest;
import com.honeychain.entity.Beekeeper;
import com.honeychain.service.BeekeeperService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beekeepers")
public class BeekeeperController {

    private final BeekeeperService beekeeperService;

    public BeekeeperController(BeekeeperService beekeeperService) {
        this.beekeeperService = beekeeperService;
    }

    @PostMapping
    public ResponseEntity<Beekeeper> register(@Valid @RequestBody RegisterBeekeeperRequest req) {
        Beekeeper saved = beekeeperService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
