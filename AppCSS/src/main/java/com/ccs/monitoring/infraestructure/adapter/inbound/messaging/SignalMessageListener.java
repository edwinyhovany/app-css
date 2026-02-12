package com.ccs.monitoring.infrastructure.adapter.in.messaging;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.domain.port.in.HandleEmergencyUseCase;
import com.ccs.monitoring.domain.port.in.ProcessSignalUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SignalMessageListener {
    
    private static final Logger log = LoggerFactory.getLogger(SignalMessageListener.class);
    
    private final ProcessSignalUseCase processSignalUseCase;
    private final HandleEmergencyUseCase handleEmergencyUseCase;
    
    public SignalMessageListener(
            ProcessSignalUseCase processSignalUseCase,
            HandleEmergencyUseCase handleEmergencyUseCase) {
        this.processSignalUseCase = processSignalUseCase;
        this.handleEmergencyUseCase = handleEmergencyUseCase;
    }
    
    @RabbitListener(
        queues = RabbitMQConfig.EMERGENCY_QUEUE,
        containerFactory = "emergencyListenerFactory"
    )
    public void handleEmergencySignal(SignalDTO signalDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.warn("🚨 EMERGENCY signal received: vehicle={}", signalDTO.getVehicleId());
            handleEmergencyUseCase.handleEmergency(signalDTO);
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Emergency processed in {}ms", duration);
            
            if (duration > 2000) {
                log.error("⚠️ Emergency processing exceeded 2s: {}ms", duration);
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing emergency signal", e);
            throw e;
        }
    }
    
    @RabbitListener(
        queues = RabbitMQConfig.SIGNALS_QUEUE,
        containerFactory = "signalsListenerFactory"
    )
    public void handleNormalSignal(SignalDTO signalDTO) {
        try {
            log.debug("📡 Normal signal received: vehicle={}", signalDTO.getVehicleId());
            processSignalUseCase.processSignal(signalDTO);
        } catch (Exception e) {
            log.error("❌ Error processing normal signal", e);
        }
    }
}