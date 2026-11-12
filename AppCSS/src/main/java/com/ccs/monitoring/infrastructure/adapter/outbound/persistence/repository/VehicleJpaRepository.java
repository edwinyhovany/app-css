package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository;

import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, String> {
    boolean existsByLicensePlate(String licensePlate);
}