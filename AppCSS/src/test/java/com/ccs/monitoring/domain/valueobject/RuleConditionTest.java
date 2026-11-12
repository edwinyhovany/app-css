package com.ccs.monitoring.domain.valueobject;

import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.RuleCondition.ConditionType;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import com.ccs.monitoring.domain.valueobject.SignalType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RuleConditionTest {

    private Signal buildSignal(BigDecimal speed, BigDecimal temperature, boolean emergency) {
        return Signal.builder()
                .vehicleId(new VehicleId("VEH001"))
                .signalType(SignalType.NORMAL)
                .timestamp(LocalDateTime.now())
                .speed(speed)
                .temperature(temperature)
                .emergency(emergency)
                .build();
    }

    // ---------------- THRESHOLD - SPEED ----------------

    @Test
    void shouldEvaluateSpeedGreaterThan() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", ">", "100");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertTrue(condition.evaluate(signal));
    }

    @Test
    void shouldEvaluateSpeedLessThan() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", "<", "200");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertTrue(condition.evaluate(signal));
    }

    @Test
    void shouldEvaluateSpeedEqual() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", "==", "150");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertTrue(condition.evaluate(signal));
    }

    @Test
    void shouldReturnFalseForInvalidOperator() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", "^>", "100");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertFalse(condition.evaluate(signal));
    }

    @Test
    void shouldReturnFalseWhenOperatorIsNull() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", null, "100");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertFalse(condition.evaluate(signal));
    }

    @Test
    void shouldReturnFalseWhenValueIsInvalidNumber() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "speed", ">", "abc");

        Signal signal = buildSignal(BigDecimal.valueOf(150), null, false);

        assertFalse(condition.evaluate(signal));
    }

    // ---------------- THRESHOLD - TEMPERATURE ----------------

    @Test
    void shouldEvaluateTemperatureGreaterThan() {
        RuleCondition condition =
                new RuleCondition(ConditionType.THRESHOLD, "temperature", ">", "30");

        Signal signal = buildSignal(null, BigDecimal.valueOf(40), false);

        assertTrue(condition.evaluate(signal));
    }

    // ---------------- EMERGENCY ----------------

    @Test
    void shouldEvaluateEmergencyCondition() {
        RuleCondition condition =
                new RuleCondition(ConditionType.EMERGENCY, null, null, null);

        Signal signal = buildSignal(null, null, true);

        assertTrue(condition.evaluate(signal));
    }

    // ---------------- TIME_BASED & GEOFENCE ----------------

    @Test
    void shouldReturnFalseForTimeBased() {
        RuleCondition condition =
                new RuleCondition(ConditionType.TIME_BASED, null, null, null);

        Signal signal = buildSignal(null, null, false);

        assertFalse(condition.evaluate(signal));
    }

    @Test
    void shouldReturnFalseForGeofence() {
        RuleCondition condition =
                new RuleCondition(ConditionType.GEOFENCE, null, null, null);

        Signal signal = buildSignal(null, null, false);

        assertFalse(condition.evaluate(signal));
    }
    // -------- TEMPERATURE - OPERADORES ---------------
    @Test
void shouldEvaluateTemperatureLessThan() {
    RuleCondition condition =
            new RuleCondition(ConditionType.THRESHOLD, "temperature", "<", "50");

    Signal signal = buildSignal(null, BigDecimal.valueOf(40), false);

    assertTrue(condition.evaluate(signal));
}

@Test
void shouldEvaluateTemperatureGreaterOrEqual() {
    RuleCondition condition =
            new RuleCondition(ConditionType.THRESHOLD, "temperature", ">=", "40");

    Signal signal = buildSignal(null, BigDecimal.valueOf(40), false);

    assertTrue(condition.evaluate(signal));
}

@Test
void shouldEvaluateTemperatureLessOrEqual() {
    RuleCondition condition =
            new RuleCondition(ConditionType.THRESHOLD, "temperature", "<=", "40");

    Signal signal = buildSignal(null, BigDecimal.valueOf(40), false);

    assertTrue(condition.evaluate(signal));
}

@Test
void shouldReturnFalseForUnknownParameter() {
    RuleCondition condition =
            new RuleCondition(ConditionType.THRESHOLD, "unknown", ">", "10");

    Signal signal = buildSignal(BigDecimal.valueOf(10), BigDecimal.valueOf(10), false);

    assertFalse(condition.evaluate(signal));
}
}