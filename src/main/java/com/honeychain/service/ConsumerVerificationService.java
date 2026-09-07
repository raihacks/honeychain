package com.honeychain.service;

import com.honeychain.dto.ChainVerificationResult;
import com.honeychain.dto.ConsumerVerificationView;
import com.honeychain.dto.LedgerTrailEntry;
import com.honeychain.entity.Batch;
import com.honeychain.entity.BatchStatus;
import com.honeychain.entity.LedgerRecord;
import com.honeychain.entity.SensorReading;
import com.honeychain.repository.BatchRepository;
import com.honeychain.repository.LedgerRecordRepository;
import com.honeychain.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Builds the read-only view a consumer sees after scanning a batch's QR
 * code: who made it, where, when it was harvested, whether the hive looked
 * healthy at the time, and the hash trail proving the record hasn't been
 * altered since.
 */
@Service
public class ConsumerVerificationService {

    // Simple, transparent thresholds for a "healthy hive" reading. Not a
    // real diagnostic model - just enough to flag obviously abnormal
    // conditions until the AI health-detection service exists.
    private static final double TEMP_MIN = 30.0;
    private static final double TEMP_MAX = 38.0;
    private static final double HUMIDITY_MIN = 40.0;
    private static final double HUMIDITY_MAX = 70.0;

    private final BatchRepository batchRepository;
    private final LedgerRecordRepository ledgerRecordRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final BatchService batchService;

    public ConsumerVerificationService(BatchRepository batchRepository,
                                        LedgerRecordRepository ledgerRecordRepository,
                                        SensorReadingRepository sensorReadingRepository,
                                        BatchService batchService) {
        this.batchRepository = batchRepository;
        this.ledgerRecordRepository = ledgerRecordRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.batchService = batchService;
    }

    @Transactional(readOnly = true)
    public ConsumerVerificationView buildView(Long batchId) {
        ConsumerVerificationView view = new ConsumerVerificationView();

        Optional<Batch> maybeBatch = batchRepository.findById(batchId);
        if (maybeBatch.isEmpty()) {
            view.setFound(false);
            return view;
        }
        Batch batch = maybeBatch.get();
        view.setFound(true);
        view.setBatchId(batch.getId());
        view.setBatchCode(batch.getBatchCode());
        view.setStatus(batch.getStatus().name());
        view.setQuantityKg(batch.getQuantityKg());

        view.setBeekeeperName(batch.getHive().getBeekeeper().getName());
        view.setBeekeeperVillage(batch.getHive().getBeekeeper().getVillage());

        view.setHiveCode(batch.getHive().getHiveCode());
        view.setHiveLocation(formatLocation(batch.getHive().getLatitude(), batch.getHive().getLongitude()));

        List<LedgerRecord> chain = ledgerRecordRepository.findByBatchIdOrderBySequenceNoAsc(batchId);

        view.setHarvestDate(
                chain.stream()
                        .filter(r -> r.getStatus() == BatchStatus.HARVESTED)
                        .findFirst()
                        .map(r -> r.getCreatedAt().toString())
                        .orElse("Not yet harvested")
        );

        view.setLedgerTrail(
                chain.stream()
                        .map(r -> new LedgerTrailEntry(
                                r.getSequenceNo(),
                                r.getStatus().name(),
                                r.getCreatedAt().toString(),
                                r.getCurrentHash()))
                        .collect(Collectors.toList())
        );

        ChainVerificationResult verification = batchService.verifyChain(batchId);
        view.setChainValid(verification.isValid());
        view.setChainIssues(verification.getIssues());

        applySensorStatus(view, batch);

        return view;
    }

    private void applySensorStatus(ConsumerVerificationView view, Batch batch) {
        List<SensorReading> readings =
                sensorReadingRepository.findByHiveIdOrderByRecordedAtDesc(batch.getHive().getId());

        if (readings.isEmpty()) {
            view.setSensorStatus("No sensor data yet");
            return;
        }

        SensorReading latest = readings.get(0);
        view.setLastTemperatureC(latest.getTemperatureC());
        view.setLastHumidityPct(latest.getHumidityPct());
        view.setLastWeightKg(latest.getWeightKg());
        view.setLastReadingAt(latest.getRecordedAt().toString());

        boolean tempOk = latest.getTemperatureC() == null
                || (latest.getTemperatureC() >= TEMP_MIN && latest.getTemperatureC() <= TEMP_MAX);
        boolean humidityOk = latest.getHumidityPct() == null
                || (latest.getHumidityPct() >= HUMIDITY_MIN && latest.getHumidityPct() <= HUMIDITY_MAX);

        view.setSensorStatus(tempOk && humidityOk ? "Healthy" : "Needs Attention");
    }

    private String formatLocation(Double lat, Double lon) {
        if (lat == null || lon == null) {
            return "Location not recorded";
        }
        return String.format("%.4f, %.4f", lat, lon);
    }
}
