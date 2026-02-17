package com.ccs.monitoring.application.service;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.ccs.monitoring.application.mapper.SignalMapper;
import com.ccs.monitoring.domain.model.*;
import com.ccs.monitoring.domain.port.out.*;
import com.ccs.monitoring.domain.service.RuleEngine;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class ProcessSignalServiceTest {

    private SignalRepository signalRepository;
    private RuleRepository ruleRepository;
    private AlertRepository alertRepository;
    private NotificationService notificationService;
    private CacheService cacheService;
    private RuleEngine ruleEngine;
    private SignalMapper signalMapper;

    private ProcessSignalService service;

    @BeforeEach
    void setup() {
        signalRepository = mock(SignalRepository.class);
        ruleRepository = mock(RuleRepository.class);
        alertRepository = mock(AlertRepository.class);
        notificationService = mock(NotificationService.class);
        cacheService = mock(CacheService.class);
        ruleEngine = mock(RuleEngine.class);
        signalMapper = new SignalMapper();

        service = new ProcessSignalService(
                signalRepository,
                ruleRepository,
                alertRepository,
                notificationService,
                cacheService,
                ruleEngine,
                signalMapper
        );
    }

    @Test
    void shouldProcessSignalAndGenerateAlert() {

        SignalDTO dto = new SignalDTO();
        dto.setVehicleId("VEH001");
        dto.setSignalType("NORMAL");
        dto.setTimestamp(LocalDateTime.now());
        dto.setSpeed(BigDecimal.valueOf(150));
        dto.setEmergency(false);

        Signal signal = signalMapper.toDomain(dto);

        when(signalRepository.save(any())).thenReturn(signal);

        when(cacheService.getRules(any())).thenReturn(null);

        Rule rule = mock(Rule.class);
        when(rule.getId()).thenReturn(1L);

        when(ruleRepository.findActiveRulesByVehicleId(any()))
                .thenReturn(List.of(rule));

        Alert alert = mock(Alert.class);
        when(alert.getRuleId()).thenReturn(1L);
        when(alert.getMessage()).thenReturn("Speed exceeded");

        when(ruleEngine.evaluateRules(any(), any()))
                .thenReturn(List.of(alert));

        service.processSignal(dto);

        verify(signalRepository).save(any());
        verify(alertRepository).save(alert);
        verify(ruleRepository).findActiveRulesByVehicleId(any());
        verify(ruleEngine).evaluateRules(any(), any());
    }
}
