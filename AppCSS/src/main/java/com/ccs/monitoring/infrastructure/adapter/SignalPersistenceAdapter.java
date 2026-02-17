package com.ccs.monitoring.infrastructure.adapter;

import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.port.out.SignalRepository;
import com.ccs.monitoring.domain.valueobject.Coordinates;
import com.ccs.monitoring.domain.valueobject.SignalType;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.SignalEntity;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository.SignalJpaRepository;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SignalPersistenceAdapter implements SignalRepository {
    
    private final SignalJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;
    
    public SignalPersistenceAdapter(SignalJpaRepository jpaRepository,ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
         this.objectMapper = objectMapper;
    }
    
    @Override
    public Signal save(Signal signal) {
        SignalEntity entity = toEntity(signal);
        SignalEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Signal> findById(Long id) {
        return jpaRepository.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public List<Signal> findByVehicleId(VehicleId vehicleId) {
        return jpaRepository.findByVehicleId(vehicleId.getValue())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Signal> findByVehicleIdAndTimestampBetween(
            VehicleId vehicleId, 
            LocalDateTime start, 
            LocalDateTime end) {
        return jpaRepository.findByVehicleIdAndTimestampBetween(
                vehicleId.getValue(), start, end)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    private JsonNode toJsonNode(Map<String, Object> map) {
    return map == null ? null : objectMapper.valueToTree(map);
}

private Map<String, Object> toMap(JsonNode node) {
    return node == null ? null :
           objectMapper.convertValue(
               node,
               new TypeReference<Map<String, Object>>() {}
           );
}    
    // Mappers
    private SignalEntity toEntity(Signal signal) {
        SignalEntity entity = new SignalEntity();
        entity.setId(signal.getId());
        entity.setVehicleId(signal.getVehicleId().getValue());
        entity.setSignalType(signal.getSignalType().name());
        entity.setTimestamp(signal.getTimestamp());
        
        if (signal.getCoordinates() != null) {
            entity.setLatitude(signal.getCoordinates().getLatitude());
            entity.setLongitude(signal.getCoordinates().getLongitude());
        }
        
        entity.setSpeed(signal.getSpeed());
        entity.setDirection(signal.getDirection());
        entity.setTemperature(signal.getTemperature());
        entity.setCargoStatus(signal.getCargoStatus());
        entity.setEmergency(signal.isEmergency());
      //  entity.setMetadata(signal.getMetadata());
        entity.setMetadata(toJsonNode(signal.getMetadata()));

        return entity;
    }
    
    private Signal toDomain(SignalEntity entity) {
        Coordinates coordinates = null;
        if (entity.getLatitude() != null && entity.getLongitude() != null) {
            coordinates = new Coordinates(entity.getLatitude(), entity.getLongitude());
        }
        
        return Signal.builder()
            .id(entity.getId())
            .vehicleId(new VehicleId(entity.getVehicleId()))
            .signalType(SignalType.valueOf(entity.getSignalType()))
            .timestamp(entity.getTimestamp())
            .coordinates(coordinates)
            .speed(entity.getSpeed())
            .direction(entity.getDirection())
            .temperature(entity.getTemperature())
            .cargoStatus(entity.getCargoStatus())
            .emergency(entity.getEmergency())
           // .metadata(entity.getMetadata())
            .metadata(toMap(entity.getMetadata()))
            .build();
    }
}