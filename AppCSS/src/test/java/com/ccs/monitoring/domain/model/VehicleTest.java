package com.ccs.monitoring.domain.model;

import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    @Test
    void shouldCreateVehicle() {
        VehicleId id = new VehicleId("VEH001");
        Vehicle vehicle = new Vehicle(id, "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001");
        assertEquals(id, vehicle.getVehicleId());
        assertEquals("ABC123", vehicle.getLicensePlate());
        assertTrue(vehicle.isActive());
    }
    @Test
    void shouldUpdateLastSignal() {
        Vehicle vehicle = new Vehicle(new VehicleId("VEH001"), "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001");
        LocalDateTime now = LocalDateTime.now();
        vehicle.updateLastSignal(now);
        assertEquals(now, vehicle.getLastSignalTime());
    }
    @Test
    void shouldDeactivateVehicle() {
        Vehicle vehicle = new Vehicle(new VehicleId("VEH001"), "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001");
        vehicle.deactivate();
        assertFalse(vehicle.isActive());
    }
}
