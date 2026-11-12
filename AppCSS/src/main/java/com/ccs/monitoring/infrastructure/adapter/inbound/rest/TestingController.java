package com.ccs.monitoring.infrastructure.adapter.inbound.rest;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/testing")
public class TestingController {
    
    private static final Logger log = LoggerFactory.getLogger(TestingController.class);
    
    private final RabbitTemplate rabbitTemplate;
    private final JdbcTemplate jdbcTemplate;
    
public TestingController(RabbitTemplate rabbitTemplate, JdbcTemplate jdbcTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.jdbcTemplate = jdbcTemplate;
}
    
    /**
     * TEST 1: Throughput - 500 señales/segundo
     * 
     * Ejemplos:
     * - 10 segundos: /testing/throughput?signalsPerSecond=500&durationSeconds=10
     * - 2 minutos: /testing/throughput?signalsPerSecond=500&durationSeconds=120
     */
    @PostMapping("/throughput")
    public ResponseEntity<Map<String, Object>> testThroughput(
            @RequestParam(defaultValue = "500") int signalsPerSecond,
            @RequestParam(defaultValue = "10") int durationSeconds) {
        
        log.warn("🧪 THROUGHPUT TEST STARTED: {} signals/s for {} seconds", 
            signalsPerSecond, durationSeconds);
        
        long startTime = System.currentTimeMillis();
        int totalSignals = signalsPerSecond * durationSeconds;
        int signalsSent = 0;
        
        List<String> vehicles = Arrays.asList(
            "VEH001", "VEH002", "VEH003", "VEH004", "VEH005",
            "VEH006", "VEH007", "VEH008", "VEH009", "VEH010"
        );
        
        try {
            // Enviar en batches para mantener throughput
            int batchSize = 50;
            long delayBetweenBatches = Math.max(1, (1000 * batchSize) / signalsPerSecond);
            
            for (int i = 0; i < totalSignals; i += batchSize) {
                int remaining = Math.min(batchSize, totalSignals - i);
                
                for (int j = 0; j < remaining; j++) {
                    String vehicleId = vehicles.get(
                        ThreadLocalRandom.current().nextInt(vehicles.size())
                    );
                    
                    SignalDTO signal = createRandomSignal(vehicleId);
                    
                    rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        "signal.normal",
                        signal,
                        message -> {
                            message.getMessageProperties().setHeader("__TypeId__", "signal");
                            return message;
                        }
                    );
                    
                    signalsSent++;
                }
                
                // Pequeña pausa para mantener throughput
                if (i + batchSize < totalSignals) {
                    try {
                        Thread.sleep(delayBetweenBatches);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
                // Log de progreso cada 5000 señales
                if (signalsSent % 5000 == 0) {
                    log.info("📊 Progress: {} signals sent", signalsSent);
                }
            }
            
        } catch (Exception e) {
            log.error("❌ Error during throughput test: {}", e.getMessage(), e);
        }
        
        long duration = System.currentTimeMillis() - startTime;
        double actualThroughput = (signalsSent * 1000.0) / duration;
        double targetThroughput = signalsPerSecond;
        boolean passed = actualThroughput >= (targetThroughput * 0.9); // 90% del objetivo
        
        Map<String, Object> response = new HashMap<>();
        response.put("targetThroughput", signalsPerSecond + " signals/s");
        response.put("durationSeconds", durationSeconds);
        response.put("totalSignalsSent", signalsSent);
        response.put("actualDurationMs", duration);
        response.put("actualThroughput", String.format("%.2f signals/s", actualThroughput));
        response.put("status", passed ? "✅ PASSED" : "⚠️ BELOW TARGET");
        response.put("efficiency", String.format("%.1f%%", (actualThroughput / targetThroughput) * 100));
        
        log.warn("📊 THROUGHPUT TEST COMPLETED: {} signals in {}ms ({} signals/s) - {}", 
            signalsSent, duration, String.format("%.2f", actualThroughput), 
            passed ? "PASSED" : "BELOW TARGET");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * TEST 2: Escenario completo - Camión refrigerado
     */
    @PostMapping("/scenario/refrigerated-truck/{vehicleId}")
    public ResponseEntity<Map<String, Object>> refrigeratedTruckScenario(
            @PathVariable String vehicleId) {
        
        log.warn("🧪 SCENARIO TEST STARTED: Refrigerated truck - {}", vehicleId);
        
        List<Map<String, Object>> events = new ArrayList<>();
        
        // Evento 1: Operación normal
        log.info("📡 Event 1: Normal operation");
        SignalDTO signal1 = new SignalDTO();
        signal1.setVehicleId(vehicleId);
        signal1.setSignalType("NORMAL");
        signal1.setTimestamp(LocalDateTime.now());
        signal1.setSpeed(BigDecimal.valueOf(80));
        signal1.setTemperature(BigDecimal.valueOf(2));
        signal1.setCargoStatus("REFRIGERATED");
        signal1.setLatitude(BigDecimal.valueOf(4.6097));
        signal1.setLongitude(BigDecimal.valueOf(-74.0817));
        signal1.setEmergency(false);
        
        sendNormalSignal(signal1);
        events.add(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "event", "Normal operation",
            "speed", 80,
            "temp", 2,
            "alert", "None"
        ));
        
        sleep(2000);
        
        // Evento 2: Exceso de velocidad
        log.warn("⚠️ Event 2: Speed violation");
        SignalDTO signal2 = new SignalDTO();
        signal2.setVehicleId(vehicleId);
        signal2.setSignalType("NORMAL");
        signal2.setTimestamp(LocalDateTime.now());
        signal2.setSpeed(BigDecimal.valueOf(120)); // EXCEDE 90
        signal2.setTemperature(BigDecimal.valueOf(3));
        signal2.setCargoStatus("REFRIGERATED");
        signal2.setLatitude(BigDecimal.valueOf(4.6200));
        signal2.setLongitude(BigDecimal.valueOf(-74.0900));
        signal2.setEmergency(false);
        
        sendNormalSignal(signal2);
        events.add(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "event", "⚠️ SPEED VIOLATION",
            "speed", 120,
            "temp", 3,
            "alert", "SMS + Email sent to owner"
        ));
        
        sleep(3000);
        
        // Evento 3: Temperatura fuera de rango
        log.warn("🌡️ Event 3: Temperature violation");
        SignalDTO signal3 = new SignalDTO();
        signal3.setVehicleId(vehicleId);
        signal3.setSignalType("NORMAL");
        signal3.setTimestamp(LocalDateTime.now());
        signal3.setSpeed(BigDecimal.valueOf(85));
        signal3.setTemperature(BigDecimal.valueOf(10)); // EXCEDE 5
        signal3.setCargoStatus("REFRIGERATED");
        signal3.setLatitude(BigDecimal.valueOf(4.6300));
        signal3.setLongitude(BigDecimal.valueOf(-74.1000));
        signal3.setEmergency(false);
        
        sendNormalSignal(signal3);
        events.add(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "event", "🌡️ TEMPERATURE VIOLATION",
            "speed", 85,
            "temp", 10,
            "alert", "SMS + Alarm triggered"
        ));
        
        sleep(2000);
        
        // Evento 4: EMERGENCIA
        log.error("🚨 Event 4: EMERGENCY - Panic button");
        SignalDTO signal4 = new SignalDTO();
        signal4.setVehicleId(vehicleId);
        signal4.setSignalType("PANIC_BUTTON");
        signal4.setTimestamp(LocalDateTime.now());
        signal4.setSpeed(BigDecimal.valueOf(0));
        signal4.setTemperature(BigDecimal.valueOf(12));
        signal4.setCargoStatus("REFRIGERATED");
        signal4.setLatitude(BigDecimal.valueOf(4.6400));
        signal4.setLongitude(BigDecimal.valueOf(-74.1100));
        signal4.setEmergency(true);
        
        sendEmergencySignal(signal4);
        events.add(Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "event", "🚨 EMERGENCY - PANIC BUTTON",
            "speed", 0,
            "temp", 12,
            "alert", "Authorities called + SMS + Owner notified"
        ));
        
        Map<String, Object> response = new HashMap<>();
        response.put("scenario", "Refrigerated Truck Complete Journey");
        response.put("vehicleId", vehicleId);
        response.put("totalEvents", 4);
        response.put("rulesTriggered", 3);
        response.put("events", events);
        response.put("status", "✅ Scenario completed successfully");
        
        log.warn("✅ SCENARIO TEST COMPLETED");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * TEST 3: Health check del módulo de testing
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> testingHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("module", "Testing Controller");
        health.put("availableTests", Arrays.asList(
            "POST /api/v1/testing/throughput",
            "POST /api/v1/testing/scenario/refrigerated-truck/{vehicleId}"
        ));
        return ResponseEntity.ok(health);
    }
    // Crear Regla
@PostMapping("/rules")
public ResponseEntity<Map<String, Object>> createTestRule(@RequestBody Map<String, Object> request) {
    
    log.info("🧪 TEST: Creating rule - {}", request.get("name"));
    
    try {
        String sql = """
            INSERT INTO operational.rules 
            (vehicle_id, name, condition_type, condition_parameter, 
             condition_operator, condition_value, actions, active, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?::jsonb, ?, CURRENT_TIMESTAMP)
            RETURNING id
            """;
        
        List<String> actions = (List<String>) request.getOrDefault("actions", 
            Arrays.asList("SEND_SMS"));
        String actionsJson = "[" + actions.stream()
            .map(a -> "\"" + a + "\"")
            .collect(Collectors.joining(",")) + "]";
        
        Long ruleId = jdbcTemplate.queryForObject(
            sql,
            Long.class,
            request.get("vehicleId"),
            request.get("name"),
            request.get("conditionType"),
            request.get("conditionParameter"),
            request.get("conditionOperator"),
            request.get("conditionValue"),
            actionsJson,
            request.getOrDefault("active", true)
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "✅ Rule created");
        response.put("id", ruleId);
        response.put("vehicleId", request.get("vehicleId"));
        response.put("name", request.get("name"));
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        log.error("❌ Error: {}", e.getMessage());
        return ResponseEntity.status(500)
            .body(Map.of("error", e.getMessage()));
    }
}
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    private SignalDTO createRandomSignal(String vehicleId) {
        SignalDTO signal = new SignalDTO();
        signal.setVehicleId(vehicleId);
        signal.setSignalType("NORMAL");
        signal.setTimestamp(LocalDateTime.now());
        signal.setLatitude(BigDecimal.valueOf(
            4.6097 + ThreadLocalRandom.current().nextDouble(-0.1, 0.1)
        ));
        signal.setLongitude(BigDecimal.valueOf(
            -74.0817 + ThreadLocalRandom.current().nextDouble(-0.1, 0.1)
        ));
        signal.setSpeed(BigDecimal.valueOf(
            ThreadLocalRandom.current().nextInt(40, 100)
        ));
        signal.setEmergency(false);
        return signal;
    }
    
    private void sendNormalSignal(SignalDTO signal) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            "signal.normal",
            signal,
            message -> {
                message.getMessageProperties().setHeader("__TypeId__", "signal");
                return message;
            }
        );
    }
    
    private void sendEmergencySignal(SignalDTO signal) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            "signal.emergency",
            signal,
            message -> {
                message.getMessageProperties().setPriority(10);
                message.getMessageProperties().setHeader("__TypeId__", "signal");
                return message;
            }
        );
    }
    
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
