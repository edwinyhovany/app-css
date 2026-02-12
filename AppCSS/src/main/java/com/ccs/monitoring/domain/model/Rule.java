package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.RuleCondition;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rule {
    private final Long id;
    private final VehicleId vehicleId;
    private final String name;
    private final RuleCondition condition;
    private final List<RuleAction> actions;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime lastExecutedAt;
    
    public Rule(Long id, VehicleId vehicleId, String name, RuleCondition condition) {
        this.id = id;
        this.vehicleId = Objects.requireNonNull(vehicleId, "VehicleId cannot be null");
        this.name = Objects.requireNonNull(name, "Rule name cannot be null");
        this.condition = Objects.requireNonNull(condition, "Condition cannot be null");
        this.actions = new ArrayList<>();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }
    
    public boolean evaluate(Signal signal) {
        if (!active) {
            return false;
        }
        return condition.evaluate(signal);
    }
    
    public void addAction(RuleAction action) {
        this.actions.add(Objects.requireNonNull(action, "Action cannot be null"));
    }
    
    public void execute() {
        this.lastExecutedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.active = true;
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    // Getters
    public Long getId() { return id; }
    public VehicleId getVehicleId() { return vehicleId; }
    public String getName() { return name; }
    public RuleCondition getCondition() { return condition; }
    public List<RuleAction> getActions() { return new ArrayList<>(actions); }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastExecutedAt() { return lastExecutedAt; }
    
    public enum RuleAction {
        SEND_SMS,
        SEND_EMAIL,
        CALL_AUTHORITIES,
        NOTIFY_OWNER,
        TRIGGER_ALARM,
        LOG_EVENT
    }
}