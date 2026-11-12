package com.ccs.monitoring.infrastructure.adapter.in.rest;

import com.ccs.monitoring.application.dto.SignalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class SignalControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldAcceptValidSignal() throws Exception {
        // Given
        SignalDTO signalDTO = new SignalDTO();
        signalDTO.setVehicleId("VEH001");
        signalDTO.setSignalType("NORMAL");
        signalDTO.setTimestamp(LocalDateTime.now());
        signalDTO.setLatitude(BigDecimal.valueOf(4.6097));
        signalDTO.setLongitude(BigDecimal.valueOf(-74.0817));
        signalDTO.setSpeed(BigDecimal.valueOf(60));
        signalDTO.setEmergency(false);
        
        // When & Then
        mockMvc.perform(post("/api/v1/signals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signalDTO)))
            .andExpect(status().isAccepted())
            .andExpect(content().string("Signal queued for processing"));
    }
    
    @Test
    void shouldRejectInvalidSignal() throws Exception {
        // Given - signal without required fields
        SignalDTO signalDTO = new SignalDTO();
        // vehicleId is null - should fail validation
        
        // When & Then
        mockMvc.perform(post("/api/v1/signals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signalDTO)))
            .andExpect(status().isBadRequest());
    }
    @Test
    void shouldAcceptPanicButton() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/signals/panic")
                .param("vehicleId", "VEH001"))
            .andExpect(status().isAccepted())
            .andExpect(content().string("Emergency signal dispatched"));
    }
}

