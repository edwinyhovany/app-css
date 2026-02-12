package com.ccs.monitoring.application.service;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.application.mapper.SignalMapper;
import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.port.in.HandleEmergencyUseCase;
import com.ccs.monitoring.domain.port.out.AlertRepository;
import com.ccs.monitoring.domain.port.out.NotificationService;
import com.ccs.monitoring.domain.port.out.SignalRepository;
import com.ccs.monitoring.domain.valueobject.AlertLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HandleEmergencyService implements HandleEmergencyUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(HandleEmergencyService.class);
    private static final long EMERGENCY_TIMEOUT_MS = 2000;
    
    private final SignalRepository signalRepository;
    private final AlertRepository alertRepository;
    private final NotificationService notificationService;
    private final SignalMapper signalMapper;
    
    public HandleEmergencyService(
            SignalRepository signalRepository,
            AlertRepository alertRepository,
            NotificationService notificationService,
            SignalMapper signalMapper) {
        this.signalRepository = signalRepository;
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
        this.signalMapper = signalMapper;
    }
    
    @Override
    public void handleEmergency(SignalDTO signalDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.warn("🚨 EMERGENCY signal received for vehicle: {}", signalDTO.getVehicleId());
            
            // 1. Convertir y guardar señal
            Signal signal = signalMapper.toDomain(signalDTO);
            Signal savedSignal = signalRepository.save(signal);
            
            // 2. Crear alerta crítica
            Alert emergencyAlert = Alert.builder()
                .vehicleId(signal.getVehicleId())
                .signalId(savedSignal.getId())
                .ruleId(null)
                .ruleName("EMERGENCY")
                .level(AlertLevel.CRITICAL)
                .message(buildEmergencyMessage(signal))
                .build();
            
            alertRepository.save(emergencyAlert);
            
            // 3. Notificaciones inmediatas
            notificationService.callAuthorities(emergencyAlert);
            notificationService.sendSMS("+573001234567", emergencyAlert.getMessage());
            notificationService.sendPushNotification("emergency_team", emergencyAlert.getMessage());
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Emergency handled in {}ms", duration);
            
            if (duration > EMERGENCY_TIMEOUT_MS) {
                log.error("⚠️ Emergency processing exceeded {}ms: {}ms", 
                    EMERGENCY_TIMEOUT_MS, duration);
            }
            
        } catch (Exception e) {
            log.error("❌ CRITICAL: Error handling emergency for vehicle {}: {}", 
                signalDTO.getVehicleId(), e.getMessage(), e);
            throw e;
        }
    }
    
    private String buildEmergencyMessage(Signal signal) {
        StringBuilder msg = new StringBuilder();
        msg.append("🚨 EMERGENCIA - Vehículo: ").append(signal.getVehicleId());
        msg.append(" | Tipo: ").append(signal.getSignalType());
        msg.append(" | Hora: ").append(signal.getTimestamp());
        
        if (signal.getCoordinates() != null) {
            msg.append(" | Ubicación: ")
               .append(signal.getCoordinates().getLatitude())
               .append(", ")
               .append(signal.getCoordinates().getLongitude());
        }
        
        return msg.toString();
    }
}