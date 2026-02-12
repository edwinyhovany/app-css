package com.ccs.monitoring.domain.port.in;

import com.ccs.monitoring.application.dto.SignalDTO;

public interface ProcessSignalUseCase {
    void processSignal(SignalDTO signalDTO);
}

// ============================================
// File: src/main/java/com/ccs/monitoring/domain/port/in/HandleEmergencyUseCase.java
package com.ccs.monitoring.domain.port.in;

import com.ccs.monitoring.application.dto.SignalDTO;

public interface HandleEmergencyUseCase {
    void handleEmergency(SignalDTO signalDTO);
}
