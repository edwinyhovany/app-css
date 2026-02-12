package com.ccs.monitoring.application.mapper;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.Coordinates;
import com.ccs.monitoring.domain.valueobject.SignalType;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.springframework.stereotype.Component;

@Component
public class SignalMapper {
    
    public Signal toDomain(SignalDTO dto) {
        Coordinates coordinates = null;
        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            coordinates = new Coordinates(dto.getLatitude(), dto.getLongitude());
        }
        
        return Signal.builder()
            .id(dto.getId())
            .vehicleId(new VehicleId(dto.getVehicleId()))
            .signalType(SignalType.valueOf(dto.getSignalType()))
            .timestamp(dto.getTimestamp())
            .coordinates(coordinates)
            .speed(dto.getSpeed())
            .direction(dto.getDirection())
            .temperature(dto.getTemperature())
            .cargoStatus(dto.getCargoStatus())
            .emergency(dto.isEmergency())
            .metadata(dto.getMetadata())
            .build();
    }
    
    public SignalDTO toDTO(Signal signal) {
        SignalDTO dto = new SignalDTO();
        dto.setId(signal.getId());
        dto.setVehicleId(signal.getVehicleId().getValue());
        dto.setSignalType(signal.getSignalType().name());
        dto.setTimestamp(signal.getTimestamp());
        
        if (signal.getCoordinates() != null) {
            dto.setLatitude(signal.getCoordinates().getLatitude());
            dto.setLongitude(signal.getCoordinates().getLongitude());
        }
        
        dto.setSpeed(signal.getSpeed());
        dto.setDirection(signal.getDirection());
        dto.setTemperature(signal.getTemperature());
        dto.setCargoStatus(signal.getCargoStatus());
        dto.setEmergency(signal.isEmergency());
        dto.setMetadata(signal.getMetadata());
        
        return dto;
    }
}