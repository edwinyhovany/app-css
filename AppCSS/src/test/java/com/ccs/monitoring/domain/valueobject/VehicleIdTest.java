package com.ccs.monitoring.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleIdTest {
    @Test
    void shouldCreateVehicleId() {
        VehicleId vehicleId = new VehicleId("VEH001");
        assertEquals("VEH001", vehicleId.getValue());
    }
    @Test
    void shouldConvertToUpperCase() {
        VehicleId vehicleId = new VehicleId("veh001");
        assertEquals("VEH001", vehicleId.getValue());
    }
    @Test
    void shouldRejectNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new VehicleId(null));
    }
    @Test
    void shouldRejectEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> new VehicleId(""));
    }
}
