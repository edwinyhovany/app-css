package com.ccs.monitoring.domain.port.in;

import com.ccs.monitoring.application.dto.SignalDTO;

public interface ProcessSignalUseCase {
    void processSignal(SignalDTO signalDTO);
}
