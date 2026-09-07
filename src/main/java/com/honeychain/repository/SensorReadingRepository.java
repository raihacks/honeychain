package com.honeychain.repository;

import com.honeychain.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findByHiveIdOrderByRecordedAtDesc(Long hiveId);
}
