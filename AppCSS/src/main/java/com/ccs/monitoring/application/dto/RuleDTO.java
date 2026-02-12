package com.ccs.monitoring.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class RuleDTO {
    
    private Long id;
    
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;
    
    @NotBlank(message = "Rule name is required")
    private String name;
    
    @NotNull(message = "Condition type is required")
    private String conditionType;
    
    private String conditionParameter;
    private String conditionOperator;
    private String conditionValue;
    private List<String> actions;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastExecutedAt;
    
    // Constructors
    public RuleDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    
    public String getConditionParameter() { return conditionParameter; }
    public void setConditionParameter(String conditionParameter) { this.conditionParameter = conditionParameter; }
    
    public String getConditionOperator() { return conditionOperator; }
    public void setConditionOperator(String conditionOperator) { this.conditionOperator = conditionOperator; }
    
    public String getConditionValue() { return conditionValue; }
    public void setConditionValue(String conditionValue) { this.conditionValue = conditionValue; }
    
    public List<String> getActions() { return actions; }
    public void setActions(List<String> actions) { this.actions = actions; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastExecutedAt() { return lastExecutedAt; }
    public void setLastExecutedAt(LocalDateTime lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
}