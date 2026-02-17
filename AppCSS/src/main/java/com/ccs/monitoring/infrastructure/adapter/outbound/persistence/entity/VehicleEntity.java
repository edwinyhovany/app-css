package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles", schema = "operational")
public class VehicleEntity {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "license_plate", length = 20, nullable = false, unique = true)
    private String licensePlate;
    
    @Column(name = "vehicle_type", length = 20, nullable = false)
    private String vehicleType;
    
    @Column(name = "owner_id", length = 50, nullable = false)
    private String ownerId;
    
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    
    @Column(name = "last_signal_time")
    private LocalDateTime lastSignalTime;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public VehicleEntity() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getLastSignalTime() { return lastSignalTime; }
    public void setLastSignalTime(LocalDateTime lastSignalTime) { this.lastSignalTime = lastSignalTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}