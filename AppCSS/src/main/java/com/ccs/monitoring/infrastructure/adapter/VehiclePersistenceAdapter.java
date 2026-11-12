package com.ccs.monitoring.infrastructure.adapter;

import com.ccs.monitoring.domain.model.Vehicle;
import com.ccs.monitoring.domain.port.out.VehicleRepository;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.VehicleEntity;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository.VehicleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VehiclePersistenceAdapter implements VehicleRepository {
    
    private final VehicleJpaRepository jpaRepository;
    
    public VehiclePersistenceAdapter(VehicleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = toEntity(vehicle);
        VehicleEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Vehicle> findById(VehicleId vehicleId) {
        return jpaRepository.findById(vehicleId.getValue())
            .map(this::toDomain);
    }
    
    @Override
    public boolean existsById(VehicleId vehicleId) {
        return jpaRepository.existsById(vehicleId.getValue());
    }
    
    // Mappers
    private VehicleEntity toEntity(Vehicle vehicle) {
        VehicleEntity entity = new VehicleEntity();
        entity.setId(vehicle.getVehicleId().getValue());
        entity.setLicensePlate(vehicle.getLicensePlate());
        entity.setVehicleType(vehicle.getType().name());
        entity.setOwnerId(vehicle.getOwnerId());
        entity.setStatus(vehicle.getStatus().name());
        entity.setLastSignalTime(vehicle.getLastSignalTime());
        return entity;
    }
    
    private Vehicle toDomain(VehicleEntity entity) {
        return new Vehicle(
            new VehicleId(entity.getId()),
            entity.getLicensePlate(),
            Vehicle.VehicleType.valueOf(entity.getVehicleType()),
            entity.getOwnerId()
        );
    }
}