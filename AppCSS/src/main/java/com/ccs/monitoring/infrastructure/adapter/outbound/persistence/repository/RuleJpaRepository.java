package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository;

import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleJpaRepository extends JpaRepository<RuleEntity, Long> {
    
    List<RuleEntity> findByVehicleIdAndActiveTrue(String vehicleId);
    
    List<RuleEntity> findByVehicleId(String vehicleId);
}