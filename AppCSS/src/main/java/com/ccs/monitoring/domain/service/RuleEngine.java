package com.ccs.monitoring.domain.service;

import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.AlertLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngine {
    
    private static final Logger log = LoggerFactory.getLogger(RuleEngine.class);
    
    public List<Alert> evaluateRules(Signal signal, List<Rule> rules) {
        List<Alert> alerts = new ArrayList<>();
        
        log.debug("Evaluating {} rules for vehicle {}", rules.size(), signal.getVehicleId());
        
        for (Rule rule : rules) {
            try {
                if (rule.evaluate(signal)) {
                    Alert alert = createAlert(signal, rule);
                    alerts.add(alert);
                    rule.execute();
                    
                    log.info("Rule '{}' triggered for vehicle {}", 
                        rule.getName(), signal.getVehicleId());
                }
            } catch (Exception e) {
                log.error("Error evaluating rule {}: {}", rule.getId(), e.getMessage());
            }
        }
        
        return alerts;
    }
    
    private Alert createAlert(Signal signal, Rule rule) {
        return Alert.builder()
            .vehicleId(signal.getVehicleId())
            .signalId(signal.getId())
            .ruleId(rule.getId())
            .ruleName(rule.getName())
            .level(determineAlertLevel(rule, signal))
            .message(buildAlertMessage(signal, rule))
            .build();
    }
    
    private AlertLevel determineAlertLevel(Rule rule, Signal signal) {
        if (signal.isEmergency()) {
            return AlertLevel.CRITICAL;
        }
        
        if (signal.isHighSpeed() || signal.hasCargoTemperatureAlert()) {
            return AlertLevel.WARNING;
        }
        
        return AlertLevel.INFO;
    }
    
    private String buildAlertMessage(Signal signal, Rule rule) {
        StringBuilder message = new StringBuilder();
        message.append("Rule '").append(rule.getName()).append("' triggered");
        message.append(" for vehicle ").append(signal.getVehicleId());
        message.append(" at ").append(signal.getTimestamp());
        
        if (signal.isEmergency()) {
            message.append(" - EMERGENCY SIGNAL");
        }
        
        if (signal.getSpeed() != null) {
            message.append(" (Speed: ").append(signal.getSpeed()).append(" km/h)");
        }
        
        return message.toString();
    }
}
