package com.ccs.monitoring.application.mapper;

import com.ccs.monitoring.application.dto.VehicleDTO;
import com.ccs.monitoring.domain.model.Vehicle;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {
    private VehicleMapper mapper;
    @BeforeEach void setUp() { mapper = new VehicleMapper(); }
    @Test
    void shouldMapDTOToDomain() {
        VehicleDTO dto = new VehicleDTO();
        dto.setId("VEH001");
        dto.setLicensePlate("ABC123");
        dto.setVehicleType("TRUCK");
        dto.setOwnerId("OWNER001");
        Vehicle vehicle = mapper.toDomain(dto);
        assertEquals("VEH001", vehicle.getVehicleId().getValue());
    }
}
