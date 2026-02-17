package com.ccs.monitoring.infrastructure.adapter.inbound.rest;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/signals")
public class SignalController {
    
    private static final Logger log = LoggerFactory.getLogger(SignalController.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    public SignalController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @PostMapping
    public ResponseEntity<String> receiveSignal(@Valid @RequestBody SignalDTO signalDTO) {
        try {
            String routingKey = signalDTO.isEmergency() ? "signal.emergency" : "signal.normal";
            int priority = signalDTO.isEmergency() ? 10 : 5;
            
            // ✅ SOLUCIÓN: Agregar __TypeId__ header manualmente
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                signalDTO,
                message -> {
                    message.getMessageProperties().setPriority(priority);
                    message.getMessageProperties().setHeader("__TypeId__", "signal"); // ← CLAVE
                    return message;
                }
            );
            
            log.debug("Signal queued: vehicle={}, emergency={}", 
                signalDTO.getVehicleId(), signalDTO.isEmergency());
            
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Signal queued for processing");
                
        } catch (Exception e) {
            log.error("Error queueing signal: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing signal");
        }
    }
    
    @PostMapping("/panic")
    public ResponseEntity<String> panicButton(@RequestParam String vehicleId) {
        try {
            log.warn("🚨 PANIC BUTTON pressed for vehicle: {}", vehicleId);
            
            SignalDTO emergencySignal = new SignalDTO();
            emergencySignal.setVehicleId(vehicleId);
            emergencySignal.setEmergency(true);
            emergencySignal.setSignalType("PANIC_BUTTON");
            emergencySignal.setTimestamp(java.time.LocalDateTime.now());
            
            // ✅ SOLUCIÓN: Agregar __TypeId__ header
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "signal.emergency",
                emergencySignal,
                message -> {
                    message.getMessageProperties().setPriority(10);
                    message.getMessageProperties().setHeader("__TypeId__", "signal"); // ← CLAVE
                    return message;
                }
            );
            
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Emergency signal dispatched");
                
        } catch (Exception e) {
            log.error("Error processing panic button: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing emergency");
        }
    }
}