package com.ccs.monitoring.domain.valueobject;

import com.ccs.monitoring.domain.model.Signal;
import java.math.BigDecimal;
import java.util.Objects;

public class RuleCondition {
    private final ConditionType type;
    private final String parameter;
    private final String operator;
    private final String value;
    
    public RuleCondition(ConditionType type, String parameter, String operator, String value) {
        this.type = Objects.requireNonNull(type, "Condition type cannot be null");
        this.parameter = parameter;
        this.operator = operator;
        this.value = value;
    }
    
    public boolean evaluate(Signal signal) {
        switch (type) {
            case THRESHOLD:
                return evaluateThreshold(signal);
            case TIME_BASED:
                return evaluateTimeBased(signal);
            case GEOFENCE:
                return evaluateGeofence(signal);
            case EMERGENCY:
                return signal.isEmergency();
            default:
                return false;
        }
    }
    
    private boolean evaluateThreshold(Signal signal) {
        if ("speed".equals(parameter) && signal.getSpeed() != null) {
            BigDecimal signalSpeed = signal.getSpeed();
            BigDecimal thresholdValue = new BigDecimal(value);
            
            switch (operator) {
                case ">": return signalSpeed.compareTo(thresholdValue) > 0;
                case "<": return signalSpeed.compareTo(thresholdValue) < 0;
                case ">=": return signalSpeed.compareTo(thresholdValue) >= 0;
                case "<=": return signalSpeed.compareTo(thresholdValue) <= 0;
                case "=": return signalSpeed.compareTo(thresholdValue) == 0;
                default: return false;
            }
        }
        
        if ("temperature".equals(parameter) && signal.getTemperature() != null) {
            BigDecimal signalTemp = signal.getTemperature();
            BigDecimal thresholdValue = new BigDecimal(value);
            
            switch (operator) {
                case ">": return signalTemp.compareTo(thresholdValue) > 0;
                case "<": return signalTemp.compareTo(thresholdValue) < 0;
                default: return false;
            }
        }
        
        return false;
    }
    
    private boolean evaluateTimeBased(Signal signal) {
        // Implementación simple - puede expandirse
        return false;
    }
    
    private boolean evaluateGeofence(Signal signal) {
        // Implementación simple - puede expandirse
        return false;
    }
    
    // Getters
    public ConditionType getType() { return type; }
    public String getParameter() { return parameter; }
    public String getOperator() { return operator; }
    public String getValue() { return value; }
    
    public enum ConditionType {
        THRESHOLD,
        TIME_BASED,
        GEOFENCE,
        EMERGENCY
    }
}
