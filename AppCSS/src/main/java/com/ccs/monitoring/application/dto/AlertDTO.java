package com.ccs.monitoring.application.dto;

import java.time.LocalDateTime;

public class AlertDTO {
    
    private Long id;
    private String vehicleId;
    private Long signalId;
    private Long ruleId;
    private String ruleName;
    private String level;
    private String message;
    private Boolean resolved;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    
    // Constructors
    public AlertDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public Long getSignalId() { return signalId; }
    public void setSignalId(Long signalId) { this.signalId = signalId; }
    
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}