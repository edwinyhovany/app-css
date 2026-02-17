package com.ccs.monitoring.application.mapper;

import com.ccs.monitoring.application.dto.RuleDTO;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleMapperTest {

    private final RuleMapper mapper = new RuleMapper();

    @Test
    void shouldMapDTOToDomain() {

        RuleDTO dto = new RuleDTO();
        dto.setId(1L);
        dto.setVehicleId("VEH001");
        dto.setName("Speed Limit");
        dto.setConditionType("THRESHOLD");
        dto.setConditionParameter("speed");
        dto.setConditionOperator(">");
        dto.setConditionValue("120");
        dto.setActions(List.of("SEND_SMS", "SEND_EMAIL"));

        Rule rule = mapper.toDomain(dto);

        assertEquals(1L, rule.getId());
        assertEquals("VEH001", rule.getVehicleId().getValue());
        assertEquals("Speed Limit", rule.getName());
        assertEquals(2, rule.getActions().size());
    }

    @Test
    void shouldMapDomainToDTO() {

        Rule rule = new Rule(
                1L,
                new VehicleId("VEH001"),
                "Speed Limit",
                new com.ccs.monitoring.domain.valueobject.RuleCondition(
                        com.ccs.monitoring.domain.valueobject.RuleCondition.ConditionType.THRESHOLD,
                        "speed",
                        ">",
                        "120"
                )
        );

        rule.addAction(Rule.RuleAction.SEND_SMS);

        RuleDTO dto = mapper.toDTO(rule);

        assertEquals("VEH001", dto.getVehicleId());
        assertEquals("Speed Limit", dto.getName());
        assertEquals("THRESHOLD", dto.getConditionType());
        assertTrue(dto.getActions().contains("SEND_SMS"));
    }
}
