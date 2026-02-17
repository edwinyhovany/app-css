package com.ccs.monitoring.domain.service;

import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {
    
    private RuleEngine ruleEngine;
    
    @BeforeEach
    void setUp() {
        ruleEngine = new RuleEngine();
    }
    
    @Test
    void shouldGenerateAlertWhenSpeedExceedsThreshold() {
        // Given
        VehicleId vehicleId = new VehicleId("VEH001");
        
        Signal signal = Signal.builder()
            .vehicleId(vehicleId)
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(150))
            .emergency(false)
            .build();
        
        RuleCondition condition = new RuleCondition(
            RuleCondition.ConditionType.THRESHOLD,
            "speed",
            ">",
            "120"
        );
        
        Rule rule = new Rule(1L, vehicleId, "Speed Limit", condition);
        rule.addAction(Rule.RuleAction.SEND_SMS);
        
        // When
        List<Alert> alerts = ruleEngine.evaluateRules(signal, Arrays.asList(rule));
        
        // Then
        assertEquals(1, alerts.size());
        Alert alert = alerts.get(0);
        assertEquals(vehicleId, alert.getVehicleId());
        assertEquals(AlertLevel.WARNING, alert.getLevel());
    }
    
    @Test
    void shouldNotGenerateAlertWhenSpeedBelowThreshold() {
        // Given
        VehicleId vehicleId = new VehicleId("VEH001");
        
        Signal signal = Signal.builder()
            .vehicleId(vehicleId)
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(80))
            .emergency(false)
            .build();
        
        RuleCondition condition = new RuleCondition(
            RuleCondition.ConditionType.THRESHOLD,
            "speed",
            ">",
            "120"
        );
        
        Rule rule = new Rule(1L, vehicleId, "Speed Limit", condition);
        
        // When
        List<Alert> alerts = ruleEngine.evaluateRules(signal, Arrays.asList(rule));
        
        // Then
        assertEquals(0, alerts.size());
    }
}
