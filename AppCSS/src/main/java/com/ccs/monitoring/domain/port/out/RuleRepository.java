package com.ccs.monitoring.domain.port.out;

import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.util.List;
import java.util.Optional;

public interface RuleRepository {
    Rule save(Rule rule);
    Optional<Rule> findById(Long id);
    List<Rule> findActiveRulesByVehicleId(VehicleId vehicleId);
    void deleteById(Long id);
}