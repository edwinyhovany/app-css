package com.ccs.monitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class SignalDTO {
    
    private Long id;
    
    @NotNull(message = "Vehicle ID is required")
    @NotBlank(message = "Vehicle ID cannot be blank")
    private String vehicleId;
    
    @NotNull(message = "Signal type is required")
    private String signalType;
    
    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private BigDecimal latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private BigDecimal longitude;
    
    @DecimalMin(value = "0.0", message = "Speed must be >= 0")
    private BigDecimal speed;
    
    @DecimalMin(value = "0.0", message = "Direction must be >= 0")
    @DecimalMax(value = "360.0", message = "Direction must be <= 360")
    private BigDecimal direction;
    
    private BigDecimal temperature;
    private String cargoStatus;
    private boolean emergency;
    private Map<String, Object> metadata;
    // Constructors
    public SignalDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public String getSignalType() { return signalType; }
    public void setSignalType(String signalType) { this.signalType = signalType; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public BigDecimal getSpeed() { return speed; }
    public void setSpeed(BigDecimal speed) { this.speed = speed; }
    
    public BigDecimal getDirection() { return direction; }
    public void setDirection(BigDecimal direction) { this.direction = direction; }
    
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    
    public String getCargoStatus() { return cargoStatus; }
    public void setCargoStatus(String cargoStatus) { this.cargoStatus = cargoStatus; }
    
    public boolean isEmergency() { return emergency; }
    public void setEmergency(boolean emergency) { this.emergency = emergency; }
    
    public Map<String, Object> getMetadata() {    return metadata;}
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata;}
}
