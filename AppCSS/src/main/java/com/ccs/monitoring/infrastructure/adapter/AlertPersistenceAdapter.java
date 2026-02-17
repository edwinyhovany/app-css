package com.ccs.monitoring.infrastructure.adapter;

import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.port.out.AlertRepository;
import com.ccs.monitoring.domain.valueobject.AlertLevel;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.AlertEntity;

import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository.AlertJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertPersistenceAdapter implements AlertRepository {
    
    private final AlertJpaRepository jpaRepository;
    
    public AlertPersistenceAdapter(AlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Alert save(Alert alert) {
        AlertEntity entity = toEntity(alert);
        AlertEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public List<Alert> findByVehicleId(VehicleId vehicleId) {
        return jpaRepository.findByVehicleId(vehicleId.getValue())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Alert> findUnresolvedAlerts() {
        return jpaRepository.findByResolvedFalseOrderByCreatedAtDesc()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    // Mappers
    private AlertEntity toEntity(Alert alert) {
        AlertEntity entity = new AlertEntity();
        entity.setId(alert.getId());
        entity.setVehicleId(alert.getVehicleId().getValue());
        entity.setSignalId(alert.getSignalId());
        entity.setRuleId(alert.getRuleId());
        entity.setRuleName(alert.getRuleName());
        entity.setLevel(alert.getLevel().name());
        entity.setMessage(alert.getMessage());
        entity.setResolved(alert.isResolved());
        entity.setResolvedAt(alert.getResolvedAt());
        return entity;
    }
    
    private Alert toDomain(AlertEntity entity) {
        return Alert.builder()
            .id(entity.getId())
            .vehicleId(new VehicleId(entity.getVehicleId()))
            .signalId(entity.getSignalId())
            .ruleId(entity.getRuleId())
            .ruleName(entity.getRuleName())
            .level(AlertLevel.valueOf(entity.getLevel()))
            .message(entity.getMessage())
            .build();
    }
}