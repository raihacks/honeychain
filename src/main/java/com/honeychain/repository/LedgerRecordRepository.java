package com.honeychain.repository;

import com.honeychain.entity.LedgerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LedgerRecordRepository extends JpaRepository<LedgerRecord, Long> {

    List<LedgerRecord> findByBatchIdOrderBySequenceNoAsc(Long batchId);

    Optional<LedgerRecord> findTopByBatchIdOrderBySequenceNoDesc(Long batchId);
}
