package com.ccs.monitoring.domain.port.out;

import com.ccs.monitoring.domain.model.Vehicle;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(VehicleId vehicleId);
    boolean existsById(VehicleId vehicleId);
}
