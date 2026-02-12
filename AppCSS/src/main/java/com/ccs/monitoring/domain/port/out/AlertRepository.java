package com.ccs.monitoring.domain.port.out;

import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.util.List;

public interface AlertRepository {
    Alert save(Alert alert);
    List<Alert> findByVehicleId(VehicleId vehicleId);
    List<Alert> findUnresolvedAlerts();
}