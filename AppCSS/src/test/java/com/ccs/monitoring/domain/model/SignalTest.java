package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SignalTest {
    @Test
    void shouldCreateSignalWithBuilder() {
        Signal signal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(80))
            .emergency(false)
            .build();
        assertNotNull(signal);
        assertEquals("VEH001", signal.getVehicleId().getValue());
        assertFalse(signal.isEmergency());
    }
    @Test
    void shouldDetectHighSpeed() {
        Signal signal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .speed(BigDecimal.valueOf(150))
            .emergency(false)
            .build();
        assertTrue(signal.isHighSpeed());
    }
    @Test
    void shouldDetectCargoTemperatureAlert() {
        Signal signal = Signal.builder()
            .vehicleId(new VehicleId("VEH001"))
            .signalType(SignalType.NORMAL)
            .timestamp(LocalDateTime.now())
            .temperature(BigDecimal.valueOf(-10))
            .emergency(false)
            .build();
        assertTrue(signal.hasCargoTemperatureAlert());
    }
}
