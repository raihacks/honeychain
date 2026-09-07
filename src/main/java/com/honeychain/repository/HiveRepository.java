package com.honeychain.repository;

import com.honeychain.entity.Hive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HiveRepository extends JpaRepository<Hive, Long> {
    Optional<Hive> findByHiveCode(String hiveCode);
    boolean existsByHiveCode(String hiveCode);
}
