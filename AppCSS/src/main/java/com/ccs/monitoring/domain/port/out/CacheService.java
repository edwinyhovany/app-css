package com.ccs.monitoring.domain.port.out;

import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.util.List;

public interface CacheService {
    List<Rule> getRules(VehicleId vehicleId);
    void putRules(VehicleId vehicleId, List<Rule> rules);
    void invalidate(VehicleId vehicleId);
    void clearAll();
}