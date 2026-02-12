package com.ccs.monitoring.application.mapper;

import com.ccs.monitoring.application.dto.RuleDTO;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.valueobject.RuleCondition;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RuleMapper {
    
    public Rule toDomain(RuleDTO dto) {
        RuleCondition condition = new RuleCondition(
            RuleCondition.ConditionType.valueOf(dto.getConditionType()),
            dto.getConditionParameter(),
            dto.getConditionOperator(),
            dto.getConditionValue()
        );
        
        Rule rule = new Rule(
            dto.getId(),
            new VehicleId(dto.getVehicleId()),
            dto.getName(),
            condition
        );
        
        if (dto.getActions() != null) {
            dto.getActions().forEach(action -> 
                rule.addAction(Rule.RuleAction.valueOf(action))
            );
        }
        
        return rule;
    }
    
    public RuleDTO toDTO(Rule rule) {
        RuleDTO dto = new RuleDTO();
        dto.setId(rule.getId());
        dto.setVehicleId(rule.getVehicleId().getValue());
        dto.setName(rule.getName());
        dto.setConditionType(rule.getCondition().getType().name());
        dto.setConditionParameter(rule.getCondition().getParameter());
        dto.setConditionOperator(rule.getCondition().getOperator());
        dto.setConditionValue(rule.getCondition().getValue());
        dto.setActions(rule.getActions().stream()
            .map(Enum::name)
            .collect(Collectors.toList()));
        dto.setActive(rule.isActive());
        dto.setCreatedAt(rule.getCreatedAt());
        dto.setLastExecutedAt(rule.getLastExecutedAt());
        return dto;
    }
}
