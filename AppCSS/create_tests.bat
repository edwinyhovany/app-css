@echo off
chcp 65001 >nul
echo ============================================
echo 🚀 CREANDO TESTS AUTOMÁTICAMENTE
echo ============================================
echo.

set PROJECT_ROOT=C:\proyecto\app-css\AppCSS

echo 📁 Creando directorios de test...

mkdir "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\model" 2>nul
mkdir "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\valueobject" 2>nul
mkdir "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\application\mapper" 2>nul
mkdir "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\infrastructure\adapter\in\rest" 2>nul

echo ✅ Directorios creados
echo.

REM ============================================
REM DOMAIN TESTS
REM ============================================

echo 📝 Creando VehicleTest.java...
(
echo package com.ccs.monitoring.domain.model;
echo.
echo import com.ccs.monitoring.domain.valueobject.VehicleId;
echo import org.junit.jupiter.api.Test;
echo import java.time.LocalDateTime;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class VehicleTest {
echo     @Test
echo     void shouldCreateVehicle^(^) {
echo         VehicleId id = new VehicleId^("VEH001"^);
echo         Vehicle vehicle = new Vehicle^(id, "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001"^);
echo         assertEquals^(id, vehicle.getVehicleId^(^)^);
echo         assertEquals^("ABC123", vehicle.getLicensePlate^(^)^);
echo         assertTrue^(vehicle.isActive^(^)^);
echo     }
echo     @Test
echo     void shouldUpdateLastSignal^(^) {
echo         Vehicle vehicle = new Vehicle^(new VehicleId^("VEH001"^), "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001"^);
echo         LocalDateTime now = LocalDateTime.now^(^);
echo         vehicle.updateLastSignal^(now^);
echo         assertEquals^(now, vehicle.getLastSignalTime^(^)^);
echo     }
echo     @Test
echo     void shouldDeactivateVehicle^(^) {
echo         Vehicle vehicle = new Vehicle^(new VehicleId^("VEH001"^), "ABC123", Vehicle.VehicleType.TRUCK, "OWNER001"^);
echo         vehicle.deactivate^(^);
echo         assertFalse^(vehicle.isActive^(^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\model\VehicleTest.java"

echo ✅ VehicleTest.java creado
echo.

echo 📝 Creando SignalTest.java...
(
echo package com.ccs.monitoring.domain.model;
echo.
echo import com.ccs.monitoring.domain.valueobject.*;
echo import org.junit.jupiter.api.Test;
echo import java.math.BigDecimal;
echo import java.time.LocalDateTime;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class SignalTest {
echo     @Test
echo     void shouldCreateSignalWithBuilder^(^) {
echo         Signal signal = Signal.builder^(^)
echo             .vehicleId^(new VehicleId^("VEH001"^)^)
echo             .signalType^(SignalType.NORMAL^)
echo             .timestamp^(LocalDateTime.now^(^)^)
echo             .speed^(BigDecimal.valueOf^(80^)^)
echo             .emergency^(false^)
echo             .build^(^);
echo         assertNotNull^(signal^);
echo         assertEquals^("VEH001", signal.getVehicleId^(^).getValue^(^)^);
echo         assertFalse^(signal.isEmergency^(^)^);
echo     }
echo     @Test
echo     void shouldDetectHighSpeed^(^) {
echo         Signal signal = Signal.builder^(^)
echo             .vehicleId^(new VehicleId^("VEH001"^)^)
echo             .signalType^(SignalType.NORMAL^)
echo             .timestamp^(LocalDateTime.now^(^)^)
echo             .speed^(BigDecimal.valueOf^(150^)^)
echo             .emergency^(false^)
echo             .build^(^);
echo         assertTrue^(signal.isHighSpeed^(^)^);
echo     }
echo     @Test
echo     void shouldDetectCargoTemperatureAlert^(^) {
echo         Signal signal = Signal.builder^(^)
echo             .vehicleId^(new VehicleId^("VEH001"^)^)
echo             .signalType^(SignalType.NORMAL^)
echo             .timestamp^(LocalDateTime.now^(^)^)
echo             .temperature^(BigDecimal.valueOf^(-10^)^)
echo             .emergency^(false^)
echo             .build^(^);
echo         assertTrue^(signal.hasCargoTemperatureAlert^(^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\model\SignalTest.java"

echo ✅ SignalTest.java creado
echo.

echo 📝 Creando RuleTest.java...
(
echo package com.ccs.monitoring.domain.model;
echo.
echo import com.ccs.monitoring.domain.valueobject.*;
echo import org.junit.jupiter.api.Test;
echo import java.math.BigDecimal;
echo import java.time.LocalDateTime;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class RuleTest {
echo     @Test
echo     void shouldCreateRule^(^) {
echo         VehicleId vehicleId = new VehicleId^("VEH001"^);
echo         RuleCondition condition = new RuleCondition^(RuleCondition.ConditionType.THRESHOLD, "speed", "^>", "120"^);
echo         Rule rule = new Rule^(1L, vehicleId, "Speed Limit", condition^);
echo         assertEquals^(1L, rule.getId^(^)^);
echo         assertTrue^(rule.isActive^(^)^);
echo     }
echo     @Test
echo     void shouldEvaluateRule^(^) {
echo         Rule rule = new Rule^(1L, new VehicleId^("VEH001"^), "Speed Limit", new RuleCondition^(RuleCondition.ConditionType.THRESHOLD, "speed", "^>", "120"^)^);
echo         Signal signal = Signal.builder^(^).vehicleId^(new VehicleId^("VEH001"^)^).signalType^(SignalType.NORMAL^).timestamp^(LocalDateTime.now^(^)^).speed^(BigDecimal.valueOf^(150^)^).emergency^(false^).build^(^);
echo         assertTrue^(rule.evaluate^(signal^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\model\RuleTest.java"

echo ✅ RuleTest.java creado
echo.

echo 📝 Creando VehicleIdTest.java...
(
echo package com.ccs.monitoring.domain.valueobject;
echo.
echo import org.junit.jupiter.api.Test;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class VehicleIdTest {
echo     @Test
echo     void shouldCreateVehicleId^(^) {
echo         VehicleId vehicleId = new VehicleId^("VEH001"^);
echo         assertEquals^("VEH001", vehicleId.getValue^(^)^);
echo     }
echo     @Test
echo     void shouldConvertToUpperCase^(^) {
echo         VehicleId vehicleId = new VehicleId^("veh001"^);
echo         assertEquals^("VEH001", vehicleId.getValue^(^)^);
echo     }
echo     @Test
echo     void shouldRejectNullValue^(^) {
echo         assertThrows^(IllegalArgumentException.class, ^(^) -^> new VehicleId^(null^)^);
echo     }
echo     @Test
echo     void shouldRejectEmptyValue^(^) {
echo         assertThrows^(IllegalArgumentException.class, ^(^) -^> new VehicleId^(""^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\valueobject\VehicleIdTest.java"

echo ✅ VehicleIdTest.java creado
echo.

echo 📝 Creando CoordinatesTest.java...
(
echo package com.ccs.monitoring.domain.valueobject;
echo.
echo import org.junit.jupiter.api.Test;
echo import java.math.BigDecimal;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class CoordinatesTest {
echo     @Test
echo     void shouldCreateValidCoordinates^(^) {
echo         Coordinates coords = new Coordinates^(BigDecimal.valueOf^(4.6097^), BigDecimal.valueOf^(-74.0817^)^);
echo         assertEquals^(BigDecimal.valueOf^(4.6097^), coords.getLatitude^(^)^);
echo         assertEquals^(BigDecimal.valueOf^(-74.0817^), coords.getLongitude^(^)^);
echo     }
echo     @Test
echo     void shouldRejectInvalidLatitude^(^) {
echo         assertThrows^(IllegalArgumentException.class, ^(^) -^> new Coordinates^(BigDecimal.valueOf^(100^), BigDecimal.valueOf^(0^)^)^);
echo     }
echo     @Test
echo     void shouldRejectInvalidLongitude^(^) {
echo         assertThrows^(IllegalArgumentException.class, ^(^) -^> new Coordinates^(BigDecimal.valueOf^(0^), BigDecimal.valueOf^(200^)^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\domain\valueobject\CoordinatesTest.java"

echo ✅ CoordinatesTest.java creado
echo.

REM ============================================
REM APPLICATION TESTS
REM ============================================

echo 📝 Creando SignalMapperTest.java...
(
echo package com.ccs.monitoring.application.mapper;
echo.
echo import com.ccs.monitoring.application.dto.SignalDTO;
echo import com.ccs.monitoring.domain.model.Signal;
echo import com.ccs.monitoring.domain.valueobject.*;
echo import org.junit.jupiter.api.*;
echo import java.math.BigDecimal;
echo import java.time.LocalDateTime;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class SignalMapperTest {
echo     private SignalMapper mapper;
echo     @BeforeEach void setUp^(^) { mapper = new SignalMapper^(^); }
echo     @Test
echo     void shouldMapDTOToDomain^(^) {
echo         SignalDTO dto = new SignalDTO^(^);
echo         dto.setVehicleId^("VEH001"^);
echo         dto.setSignalType^("NORMAL"^);
echo         dto.setTimestamp^(LocalDateTime.now^(^)^);
echo         dto.setSpeed^(BigDecimal.valueOf^(60^)^);
echo         dto.setEmergency^(false^);
echo         Signal signal = mapper.toDomain^(dto^);
echo         assertEquals^("VEH001", signal.getVehicleId^(^).getValue^(^)^);
echo         assertFalse^(signal.isEmergency^(^)^);
echo     }
echo     @Test
echo     void shouldMapDomainToDTO^(^) {
echo         Signal signal = Signal.builder^(^).id^(1L^).vehicleId^(new VehicleId^("VEH001"^)^).signalType^(SignalType.NORMAL^).timestamp^(LocalDateTime.now^(^)^).speed^(BigDecimal.valueOf^(60^)^).emergency^(false^).build^(^);
echo         SignalDTO dto = mapper.toDTO^(signal^);
echo         assertEquals^("VEH001", dto.getVehicleId^(^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\application\mapper\SignalMapperTest.java"

echo ✅ SignalMapperTest.java creado
echo.

echo 📝 Creando VehicleMapperTest.java...
(
echo package com.ccs.monitoring.application.mapper;
echo.
echo import com.ccs.monitoring.application.dto.VehicleDTO;
echo import com.ccs.monitoring.domain.model.Vehicle;
echo import com.ccs.monitoring.domain.valueobject.VehicleId;
echo import org.junit.jupiter.api.*;
echo import static org.junit.jupiter.api.Assertions.*;
echo.
echo class VehicleMapperTest {
echo     private VehicleMapper mapper;
echo     @BeforeEach void setUp^(^) { mapper = new VehicleMapper^(^); }
echo     @Test
echo     void shouldMapDTOToDomain^(^) {
echo         VehicleDTO dto = new VehicleDTO^(^);
echo         dto.setId^("VEH001"^);
echo         dto.setLicensePlate^("ABC123"^);
echo         dto.setVehicleType^("TRUCK"^);
echo         dto.setOwnerId^("OWNER001"^);
echo         Vehicle vehicle = mapper.toDomain^(dto^);
echo         assertEquals^("VEH001", vehicle.getVehicleId^(^).getValue^(^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\application\mapper\VehicleMapperTest.java"

echo ✅ VehicleMapperTest.java creado
echo.

REM ============================================
REM INFRASTRUCTURE TESTS
REM ============================================

echo 📝 Creando HealthControllerTest.java...
(
echo package com.ccs.monitoring.infrastructure.adapter.in.rest;
echo.
echo import org.junit.jupiter.api.Test;
echo import org.springframework.beans.factory.annotation.Autowired;
echo import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
echo import org.springframework.boot.test.context.SpringBootTest;
echo import org.springframework.test.web.servlet.MockMvc;
echo import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
echo import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
echo.
echo @SpringBootTest
echo @AutoConfigureMockMvc
echo class HealthControllerTest {
echo     @Autowired private MockMvc mockMvc;
echo     @Test
echo     void shouldReturnHealthStatus^(^) throws Exception {
echo         mockMvc.perform^(get^("/api/v1/health"^)^)
echo             .andExpect^(status^(^).isOk^(^)^)
echo             .andExpect^(jsonPath^("$.status"^).value^("UP"^)^);
echo     }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\infrastructure\adapter\in\rest\HealthControllerTest.java"

echo ✅ HealthControllerTest.java creado
echo.

echo 📝 Creando CcsVehicleMonitoringApplicationTests.java...
(
echo package com.ccs.monitoring;
echo.
echo import org.junit.jupiter.api.Test;
echo import org.springframework.boot.test.context.SpringBootTest;
echo.
echo @SpringBootTest
echo class CcsVehicleMonitoringApplicationTests {
echo     @Test
echo     void contextLoads^(^) { }
echo }
) > "%PROJECT_ROOT%\src\test\java\com\ccs\monitoring\CcsVehicleMonitoringApplicationTests.java"

echo ✅ CcsVehicleMonitoringApplicationTests.java creado
echo.

echo ============================================
echo ✅ TODOS LOS TESTS CREADOS EXITOSAMENTE
echo ============================================
echo.
echo 📊 Archivos creados:
echo   - VehicleTest.java
echo   - SignalTest.java
echo   - RuleTest.java
echo   - VehicleIdTest.java
echo   - CoordinatesTest.java
echo   - SignalMapperTest.java
echo   - VehicleMapperTest.java
echo   - HealthControllerTest.java
echo   - CcsVehicleMonitoringApplicationTests.java
echo.
echo 🚀 Siguiente paso:
echo    cd %PROJECT_ROOT%
echo    mvn clean test
echo.
pause