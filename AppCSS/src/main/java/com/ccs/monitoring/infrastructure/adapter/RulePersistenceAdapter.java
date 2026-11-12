package com.ccs.monitoring.infrastructure.adapter;

import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.port.out.RuleRepository;
import com.ccs.monitoring.domain.valueobject.RuleCondition;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.entity.RuleEntity;
import com.ccs.monitoring.infrastructure.adapter.outbound.persistence.repository.RuleJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RulePersistenceAdapter implements RuleRepository {
    
    private static final Logger log = LoggerFactory.getLogger(RulePersistenceAdapter.class);
    private final RuleJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;
    
    public RulePersistenceAdapter(RuleJpaRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Rule save(Rule rule) {
        RuleEntity entity = toEntity(rule);
        RuleEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Rule> findById(Long id) {
        return jpaRepository.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public List<Rule> findActiveRulesByVehicleId(VehicleId vehicleId) {
        return jpaRepository.findByVehicleIdAndActiveTrue(vehicleId.getValue())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    // Mappers
    private RuleEntity toEntity(Rule rule) {
        RuleEntity entity = new RuleEntity();
        entity.setId(rule.getId());
        entity.setVehicleId(rule.getVehicleId().getValue());
        entity.setName(rule.getName());
        entity.setConditionType(rule.getCondition().getType().name());
        entity.setConditionParameter(rule.getCondition().getParameter());
        entity.setConditionOperator(rule.getCondition().getOperator());
        entity.setConditionValue(rule.getCondition().getValue());
        entity.setActive(rule.isActive());
        
        // Convertir actions a JSON
        try {
            List<String> actions = rule.getActions().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
            entity.setActions(objectMapper.writeValueAsString(actions));
        } catch (JsonProcessingException e) {
            log.error("Error serializing actions", e);
            entity.setActions("[]");
        }
        
        return entity;
    }
    
    private Rule toDomain(RuleEntity entity) {
        RuleCondition condition = new RuleCondition(
            RuleCondition.ConditionType.valueOf(entity.getConditionType()),
            entity.getConditionParameter(),
            entity.getConditionOperator(),
            entity.getConditionValue()
        );
        
        Rule rule = new Rule(
            entity.getId(),
            new VehicleId(entity.getVehicleId()),
            entity.getName(),
            condition
        );
        
        // Parsear actions desde JSON
        try {
            String[] actions = objectMapper.readValue(entity.getActions(), String[].class);
            Arrays.stream(actions).forEach(action -> 
                rule.addAction(Rule.RuleAction.valueOf(action))
            );
        } catch (JsonProcessingException e) {
            log.error("Error deserializing actions", e);
        }
        
        if (!entity.getActive()) {
            rule.deactivate();
        }
        
        return rule;
    }
}
