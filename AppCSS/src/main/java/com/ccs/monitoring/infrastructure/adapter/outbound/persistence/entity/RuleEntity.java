package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rules", schema = "operational")
public class RuleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vehicle_id", length = 50, nullable = false)
    private String vehicleId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "condition_type", length = 50, nullable = false)
    private String conditionType;
    
    @Column(name = "condition_parameter", length = 100)
    private String conditionParameter;
    
    @Column(name = "condition_operator", length = 10)
    private String conditionOperator;
    
    @Column(name = "condition_value", length = 100)
    private String conditionValue;
    
    @Column(name = "actions", columnDefinition = "jsonb", nullable = false)
    private String actions;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "last_executed_at")
    private LocalDateTime lastExecutedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public RuleEntity() {}
    
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
    
    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastExecutedAt() { return lastExecutedAt; }
    public void setLastExecutedAt(LocalDateTime lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
}