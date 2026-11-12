package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_signals", schema = "telemetry")
public class SignalEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vehicle_id", length = 50, nullable = false)
    private String vehicleId;
    
    @Column(name = "signal_type", length = 50, nullable = false)
    private String signalType;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "speed", precision = 5, scale = 2)
    private BigDecimal speed;
    
    @Column(name = "direction", precision = 5, scale = 2)
    private BigDecimal direction;
    
    @Column(name = "temperature", precision = 5, scale = 2)
    private BigDecimal temperature;
    
    @Column(name = "cargo_status", length = 50)
    private String cargoStatus;
    
    @Column(name = "emergency", nullable = false)
    private Boolean emergency = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode metadata;

    
    // Constructors
    public SignalEntity() {}
    
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
    
    public Boolean getEmergency() { return emergency; }
    public void setEmergency(Boolean emergency) { this.emergency = emergency; }
    
    public JsonNode getMetadata() { return metadata; }
    public void setMetadata(JsonNode metadata) { this.metadata = metadata; }
}