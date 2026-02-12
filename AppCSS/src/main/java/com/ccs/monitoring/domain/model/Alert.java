package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.AlertLevel;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.time.LocalDateTime;
import java.util.Objects;

public class Alert {
    private final Long id;
    private final VehicleId vehicleId;
    private final Long signalId;
    private final Long ruleId;
    private final String ruleName;
    private final AlertLevel level;
    private final String message;
    private boolean resolved;
    private final LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    
    private Alert(AlertBuilder builder) {
        this.id = builder.id;
        this.vehicleId = Objects.requireNonNull(builder.vehicleId, "VehicleId cannot be null");
        this.signalId = builder.signalId;
        this.ruleId = builder.ruleId;
        this.ruleName = Objects.requireNonNull(builder.ruleName, "Rule name cannot be null");
        this.level = Objects.requireNonNull(builder.level, "Alert level cannot be null");
        this.message = Objects.requireNonNull(builder.message, "Message cannot be null");
        this.resolved = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public void resolve() {
        this.resolved = true;
        this.resolvedAt = LocalDateTime.now();
    }
    
    public boolean isCritical() {
        return level == AlertLevel.CRITICAL;
    }
    
    // Getters
    public Long getId() { return id; }
    public VehicleId getVehicleId() { return vehicleId; }
    public Long getSignalId() { return signalId; }
    public Long getRuleId() { return ruleId; }
    public String getRuleName() { return ruleName; }
    public AlertLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public boolean isResolved() { return resolved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    
    public static AlertBuilder builder() {
        return new AlertBuilder();
    }
    
    public static class AlertBuilder {
        private Long id;
        private VehicleId vehicleId;
        private Long signalId;
        private Long ruleId;
        private String ruleName;
        private AlertLevel level;
        private String message;
        
        public AlertBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public AlertBuilder vehicleId(VehicleId vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        
        public AlertBuilder signalId(Long signalId) {
            this.signalId = signalId;
            return this;
        }
        
        public AlertBuilder ruleId(Long ruleId) {
            this.ruleId = ruleId;
            return this;
        }
        
        public AlertBuilder ruleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }
        
        public AlertBuilder level(AlertLevel level) {
            this.level = level;
            return this;
        }
        
        public AlertBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public Alert build() {
            return new Alert(this);
        }
    }
}
