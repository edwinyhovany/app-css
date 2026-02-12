package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.time.LocalDateTime;
import java.util.Objects;

public class Vehicle {
    private final VehicleId vehicleId;
    private String licensePlate;
    private VehicleType type;
    private String ownerId;
    private VehicleStatus status;
    private LocalDateTime lastSignalTime;
    private final LocalDateTime createdAt;
    
    public Vehicle(VehicleId vehicleId, String licensePlate, VehicleType type, String ownerId) {
        this.vehicleId = Objects.requireNonNull(vehicleId, "VehicleId cannot be null");
        this.licensePlate = Objects.requireNonNull(licensePlate, "License plate cannot be null");
        this.type = Objects.requireNonNull(type, "Vehicle type cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId, "OwnerId cannot be null");
        this.status = VehicleStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
    
    public void updateLastSignal(LocalDateTime timestamp) {
        this.lastSignalTime = timestamp;
        if (this.status == VehicleStatus.INACTIVE) {
            this.status = VehicleStatus.ACTIVE;
        }
    }
    
    public boolean isActive() {
        return this.status == VehicleStatus.ACTIVE;
    }
    
    public void deactivate() {
        this.status = VehicleStatus.INACTIVE;
    }
    
    // Getters
    public VehicleId getVehicleId() { return vehicleId; }
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getType() { return type; }
    public String getOwnerId() { return ownerId; }
    public VehicleStatus getStatus() { return status; }
    public LocalDateTime getLastSignalTime() { return lastSignalTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public enum VehicleType {
        TRUCK, BUS, TAXI, MOTORCYCLE, PRIVATE_CAR
    }
    
    public enum VehicleStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }
}
