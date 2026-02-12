import com.ccs.monitoring.application.dto.VehicleDTO;
import com.ccs.monitoring.domain.model.Vehicle;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    
    public Vehicle toDomain(VehicleDTO dto) {
        return new Vehicle(
            new VehicleId(dto.getId()),
            dto.getLicensePlate(),
            Vehicle.VehicleType.valueOf(dto.getVehicleType()),
            dto.getOwnerId()
        );
    }
    
    public VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getVehicleId().getValue());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setVehicleType(vehicle.getType().name());
        dto.setOwnerId(vehicle.getOwnerId());
        dto.setStatus(vehicle.getStatus().name());
        dto.setLastSignalTime(vehicle.getLastSignalTime());
        dto.setCreatedAt(vehicle.getCreatedAt());
        return dto;
    }
}