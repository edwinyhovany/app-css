package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository;

import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.SignalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SignalJpaRepository extends JpaRepository<SignalEntity, Long> {
    
    List<SignalEntity> findByVehicleId(String vehicleId);
    
    List<SignalEntity> findByVehicleIdAndTimestampBetween(
        String vehicleId, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    List<SignalEntity> findByEmergencyTrueOrderByTimestampDesc();
}