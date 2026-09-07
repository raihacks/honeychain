package com.honeychain.repository;

import com.honeychain.entity.Beekeeper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeekeeperRepository extends JpaRepository<Beekeeper, Long> {
    boolean existsByPhone(String phone);
}
