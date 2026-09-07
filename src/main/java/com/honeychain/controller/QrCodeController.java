package com.honeychain.controller;

import com.honeychain.service.QrCodeService;
import com.honeychain.service.NotFoundException;
import com.honeychain.repository.BatchRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batches")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final BatchRepository batchRepository;

    public QrCodeController(QrCodeService qrCodeService, BatchRepository batchRepository) {
        this.qrCodeService = qrCodeService;
        this.batchRepository = batchRepository;
    }

    // GET /api/batches/3/qr?size=300 -> PNG image encoding {base-url}/verify/3
    @GetMapping(value = "/{batchId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQr(@PathVariable Long batchId,
                                         @RequestParam(defaultValue = "300") int size) {
        if (!batchRepository.existsById(batchId)) {
            throw new NotFoundException("Batch not found: " + batchId);
        }
        String url = qrCodeService.buildVerificationUrl(batchId);
        byte[] png = qrCodeService.generateQrPng(url, size);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"batch-" + batchId + "-qr.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

    // GET /api/batches/3/qr-url -> plain text of the URL encoded in the QR, handy for debugging
    @GetMapping(value = "/{batchId}/qr-url", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getQrUrl(@PathVariable Long batchId) {
        if (!batchRepository.existsById(batchId)) {
            throw new NotFoundException("Batch not found: " + batchId);
        }
        return ResponseEntity.ok(qrCodeService.buildVerificationUrl(batchId));
    }
}
