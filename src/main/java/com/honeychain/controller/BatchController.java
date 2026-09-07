package com.honeychain.controller;

import com.honeychain.dto.ChainVerificationResult;
import com.honeychain.dto.CreateBatchRequest;
import com.honeychain.dto.UpdateBatchStatusRequest;
import com.honeychain.entity.Batch;
import com.honeychain.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<Batch> create(@Valid @RequestBody CreateBatchRequest req) {
        Batch saved = batchService.createBatch(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // e.g. PATCH /api/batches/3/status  { "newStatus": "HARVESTED" }
    @PatchMapping("/{batchId}/status")
    public ResponseEntity<Batch> updateStatus(@PathVariable Long batchId,
                                               @Valid @RequestBody UpdateBatchStatusRequest req) {
        Batch updated = batchService.transitionStatus(batchId, req.getNewStatus());
        return ResponseEntity.ok(updated);
    }

    // GET /api/batches/3/verify - recompute the hash chain and confirm no tampering
    @GetMapping("/{batchId}/verify")
    public ResponseEntity<ChainVerificationResult> verify(@PathVariable Long batchId) {
        ChainVerificationResult result = batchService.verifyChain(batchId);
        return ResponseEntity.ok(result);
    }
}
