package com.ccs.monitoring.application.service;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.application.mapper.SignalMapper;
import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.model.Signal;
import com.ccs.monitoring.domain.port.in.ProcessSignalUseCase;
import com.ccs.monitoring.domain.port.out.*;
import com.ccs.monitoring.domain.service.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProcessSignalService implements ProcessSignalUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ProcessSignalService.class);
    
    private final SignalRepository signalRepository;
    private final RuleRepository ruleRepository;
    private final AlertRepository alertRepository;
    private final NotificationService notificationService;
    private final CacheService cacheService;
    private final RuleEngine ruleEngine;
    private final SignalMapper signalMapper;
    
    public ProcessSignalService(
            SignalRepository signalRepository,
            RuleRepository ruleRepository,
            AlertRepository alertRepository,
            NotificationService notificationService,
            CacheService cacheService,
            RuleEngine ruleEngine,
            SignalMapper signalMapper) {
        this.signalRepository = signalRepository;
        this.ruleRepository = ruleRepository;
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
        this.cacheService = cacheService;
        this.ruleEngine = ruleEngine;
        this.signalMapper = signalMapper;
    }
    
    @Override
    public void processSignal(SignalDTO signalDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. Convertir DTO a entidad de dominio
            Signal signal = signalMapper.toDomain(signalDTO);
            
            // 2. Persistir señal
            Signal savedSignal = signalRepository.save(signal);
            log.debug("Signal saved: {} for vehicle {}", savedSignal.getId(), signal.getVehicleId());
            
            // 3. Obtener reglas activas (con cache)
            List<Rule> activeRules = cacheService.getRules(signal.getVehicleId());
            
            if (activeRules == null) {
                log.debug("Cache miss for vehicle {}, loading from DB", signal.getVehicleId());
                activeRules = ruleRepository.findActiveRulesByVehicleId(signal.getVehicleId());
                cacheService.putRules(signal.getVehicleId(), activeRules);
            }
            
            // 4. Evaluar reglas usando el motor de dominio
            List<Alert> alerts = ruleEngine.evaluateRules(savedSignal, activeRules);
            
            // 5. Procesar alertas generadas
            for (Alert alert : alerts) {
                alertRepository.save(alert);
                
                // 6. Enviar notificaciones
                Rule triggeredRule = activeRules.stream()
                    .filter(r -> r.getId().equals(alert.getRuleId()))
                    .findFirst()
                    .orElse(null);
                    
                if (triggeredRule != null) {
                    executeRuleActions(triggeredRule, alert);
                }
            }
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Signal processed in {}ms - {} alerts generated", duration, alerts.size());
            
        } catch (Exception e) {
            log.error("Error processing signal for vehicle {}: {}", 
                signalDTO.getVehicleId(), e.getMessage(), e);
            throw e;
        }
    }
    
    private void executeRuleActions(Rule rule, Alert alert) {
        for (Rule.RuleAction action : rule.getActions()) {
            try {
                switch (action) {
                    case SEND_SMS:
                        notificationService.sendSMS("+573001234567", alert.getMessage());
                        break;
                    case SEND_EMAIL:
                        notificationService.sendEmail(
                            "owner@ccs.com", 
                            "Alert: " + alert.getRuleName(), 
                            alert.getMessage()
                        );
                        break;
                    case CALL_AUTHORITIES:
                        notificationService.callAuthorities(alert);
                        break;
                    case NOTIFY_OWNER:
                        notificationService.sendPushNotification("owner123", alert.getMessage());
                        break;
                    default:
                        log.debug("Action {} not implemented", action);
                        break;
                }
            } catch (Exception e) {
                log.error("Error executing action {}: {}", action, e.getMessage());
            }
        }
    }
}