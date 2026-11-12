package com.ccs.monitoring.application.mapper;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.valueobject.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SignalMapperTest {
    private SignalMapper mapper;
    @BeforeEach void setUp() { mapper = new SignalMapper(); }
    @Test
    void shouldMapDTOToDomain() {
        SignalDTO dto = new SignalDTO();
        dto.setVehicleId("VEH001");
        dto.setSignalType("NORMAL");
        dto.setTimestamp(LocalDateTime.now());
        dto.setSpeed(BigDecimal.valueOf(60));
        dto.setEmergency(false);
        Signal signal = mapper.toDomain(dto);
        assertEquals("VEH001", signal.getVehicleId().getValue());
        assertFalse(signal.isEmergency());
    }
    @Test
    void shouldMapDomainToDTO() {
        Signal signal = Signal.builder().id(1L).vehicleId(new VehicleId("VEH001")).signalType(SignalType.NORMAL).timestamp(LocalDateTime.now()).speed(BigDecimal.valueOf(60)).emergency(false).build();
        SignalDTO dto = mapper.toDTO(signal);
        assertEquals("VEH001", dto.getVehicleId());
    }
}
