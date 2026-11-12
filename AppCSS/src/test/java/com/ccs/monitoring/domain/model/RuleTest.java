package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class RuleTest {
    
    @Test
    void shouldCreateRule() {
        // Given
        VehicleId vehicleId = new VehicleId("VEH001");
        RuleCondition condition = new RuleCondition(
            RuleCondition.ConditionType.THRESHOLD,
            "speed",
            ">",
            "120"
        );
        
        // When
        Rule rule = new Rule(1L, vehicleId, "Speed Limit", condition);
        
        // Then
        assertEquals(1L, rule.getId());
        assertEquals(vehicleId, rule.getVehicleId());
        assertEquals("Speed Limit", rule.getName());
        assertTrue(rule.isActive());
        assertNotNull(rule.getCreatedAt());
    }
    
    @Test
    void shouldAddAction() {
        // Given
        Rule rule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Speed Limit",
            new RuleCondition(RuleCondition.ConditionType.THRESHOLD, "speed", ">", "120")
        );
        
        // When
        rule.addAction(Rule.RuleAction.SEND_SMS);
        rule.addAction(Rule.RuleAction.SEND_EMAIL);
        
        // Then
        assertEquals(2, rule.getActions().size());
        assertTrue(rule.getActions().contains(Rule.RuleAction.SEND_SMS));
        assertTrue(rule.getActions().contains(Rule.RuleAction.SEND_EMAIL));
    }
    
    @Test
    void shouldEvaluateRuleWhenActive() {
        // Given
        Rule rule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Speed Limit",
            new RuleCondition(RuleCondition.ConditionType.THRESHOLD, "speed", ">", "120")
        );
        
        Signal signal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(150))
            .emergency(false)
            .build();
        
        // When
        boolean result = rule.evaluate(signal);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void shouldNotEvaluateRuleWhenInactive() {
        // Given
        Rule rule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Speed Limit",
            new RuleCondition(RuleCondition.ConditionType.THRESHOLD, "speed", ">", "120")
        );
        rule.deactivate();
        
        Signal signal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(150))
            .emergency(false)
            .build();
        
        // When
        boolean result = rule.evaluate(signal);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void shouldActivateAndDeactivateRule() {
        // Given
        Rule rule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Speed Limit",
            new RuleCondition(RuleCondition.ConditionType.THRESHOLD, "speed", ">", "120")
        );
        
        // When & Then
        assertTrue(rule.isActive());
        
        rule.deactivate();
        assertFalse(rule.isActive());
        
        rule.activate();
        assertTrue(rule.isActive());
    }
    
    @Test
    void shouldUpdateLastExecutedTime() {
        // Given
        Rule rule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Speed Limit",
            new RuleCondition(RuleCondition.ConditionType.THRESHOLD, "speed", ">", "120")
        );
        assertNull(rule.getLastExecutedAt());
        
        // When
        rule.execute();
        
        // Then
        assertNotNull(rule.getLastExecutedAt());
    }
    
    @Test
    void shouldEvaluateEmergencyRule() {
        // Given - ESTE TEST USA EMERGENCY CONDITION QUE SÍ FUNCIONA
        Rule emergencyRule = new Rule(
            1L,
            new VehicleId("VEH001"),
            "Emergency Detection",
            new RuleCondition(RuleCondition.ConditionType.EMERGENCY, null, null, null)
        );
        
        Signal emergencySignal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.EMERGENCY)
            .timestamp(LocalDateTime.now())
            .emergency(true)
            .build();
        
        // When
        boolean result = emergencyRule.evaluate(emergencySignal);
        
        // Then
        assertTrue(result);
    }
}