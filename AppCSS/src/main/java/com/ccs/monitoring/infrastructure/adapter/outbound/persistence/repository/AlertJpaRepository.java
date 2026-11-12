package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository;

import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {
    
    List<AlertEntity> findByVehicleId(String vehicleId);
    
    List<AlertEntity> findByResolvedFalseOrderByCreatedAtDesc();
}