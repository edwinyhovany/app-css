package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.Coordinates;
import com.ccs.monitoring.domain.valueobject.SignalType;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Signal {
    private final Long id;
    private final VehicleId vehicleId;
    private final SignalType signalType;
    private final LocalDateTime timestamp;
    private final Coordinates coordinates;
    private final BigDecimal speed;
    private final BigDecimal direction;
    private final BigDecimal temperature;
    private final String cargoStatus;
    private final boolean emergency;
    private final String metadata;
    
    private Signal(SignalBuilder builder) {
        this.id = builder.id;
        this.vehicleId = Objects.requireNonNull(builder.vehicleId, "VehicleId cannot be null");
        this.signalType = Objects.requireNonNull(builder.signalType, "SignalType cannot be null");
        this.timestamp = Objects.requireNonNull(builder.timestamp, "Timestamp cannot be null");
        this.coordinates = builder.coordinates;
        this.speed = builder.speed;
        this.direction = builder.direction;
        this.temperature = builder.temperature;
        this.cargoStatus = builder.cargoStatus;
        this.emergency = builder.emergency;
        this.metadata = builder.metadata;
    }
    
    public boolean isEmergency() {
        return emergency;
    }
    
    public boolean isHighSpeed() {
        return speed != null && speed.compareTo(BigDecimal.valueOf(120)) > 0;
    }
    
    public boolean hasCargoTemperatureAlert() {
        return temperature != null && 
               (temperature.compareTo(BigDecimal.valueOf(-5)) < 0 || 
                temperature.compareTo(BigDecimal.valueOf(30)) > 0);
    }
    
    // Getters
    public Long getId() { return id; }
    public VehicleId getVehicleId() { return vehicleId; }
    public SignalType getSignalType() { return signalType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Coordinates getCoordinates() { return coordinates; }
    public BigDecimal getSpeed() { return speed; }
    public BigDecimal getDirection() { return direction; }
    public BigDecimal getTemperature() { return temperature; }
    public String getCargoStatus() { return cargoStatus; }
    public String getMetadata() { return metadata; }
    
    public static SignalBuilder builder() {
        return new SignalBuilder();
    }
    
    public static class SignalBuilder {
        private Long id;
        private VehicleId vehicleId;
        private SignalType signalType;
        private LocalDateTime timestamp;
        private Coordinates coordinates;
        private BigDecimal speed;
        private BigDecimal direction;
        private BigDecimal temperature;
        private String cargoStatus;
        private boolean emergency;
        private String metadata;
        
        public SignalBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public SignalBuilder vehicleId(VehicleId vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        
        public SignalBuilder signalType(SignalType signalType) {
            this.signalType = signalType;
            return this;
        }
        
        public SignalBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public SignalBuilder coordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }
        
        public SignalBuilder speed(BigDecimal speed) {
            this.speed = speed;
            return this;
        }
        
        public SignalBuilder direction(BigDecimal direction) {
            this.direction = direction;
            return this;
        }
        
        public SignalBuilder temperature(BigDecimal temperature) {
            this.temperature = temperature;
            return this;
        }
        
        public SignalBuilder cargoStatus(String cargoStatus) {
            this.cargoStatus = cargoStatus;
            return this;
        }
        
        public SignalBuilder emergency(boolean emergency) {
            this.emergency = emergency;
            return this;
        }
        
        public SignalBuilder metadata(String metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public Signal build() {
            return new Signal(this);
        }
    }
}