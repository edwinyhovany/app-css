package com.ccs.monitoring.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class VehicleDTO {
    
    private String id;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotNull(message = "Vehicle type is required")
    private String vehicleType;
    
    @NotBlank(message = "Owner ID is required")
    private String ownerId;
    
    private String status;
    private LocalDateTime lastSignalTime;
    private LocalDateTime createdAt;
    
    // Constructors
    public VehicleDTO() {}
    
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
}
