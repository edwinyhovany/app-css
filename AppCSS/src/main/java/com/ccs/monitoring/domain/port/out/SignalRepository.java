import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SignalRepository {
    Signal save(Signal signal);
    Optional<Signal> findById(Long id);
    List<Signal> findByVehicleId(VehicleId vehicleId);
    List<Signal> findByVehicleIdAndTimestampBetween(
        VehicleId vehicleId, 
        LocalDateTime start, 
        LocalDateTime end
    );
}