package com.ccs.monitoring.application.service;

import com.ccs.monitoring.application.dto.RuleDTO;
import com.ccs.monitoring.application.mapper.RuleMapper;
import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.port.in.ConfigureRuleUseCase;
import com.ccs.monitoring.domain.port.out.CacheService;
import com.ccs.monitoring.domain.port.out.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigureRuleService implements ConfigureRuleUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigureRuleService.class);
    
    private final RuleRepository ruleRepository;
    private final CacheService cacheService;
    private final RuleMapper ruleMapper;
    
    public ConfigureRuleService(
            RuleRepository ruleRepository,
            CacheService cacheService,
            RuleMapper ruleMapper) {
        this.ruleRepository = ruleRepository;
        this.cacheService = cacheService;
        this.ruleMapper = ruleMapper;
    }
    
    @Override
    public RuleDTO createRule(RuleDTO ruleDTO) {
        log.info("Creating rule: {} for vehicle {}", ruleDTO.getName(), ruleDTO.getVehicleId());
        
        Rule rule = ruleMapper.toDomain(ruleDTO);
        Rule saved = ruleRepository.save(rule);
        
        // Invalidar cache para que se recarguen las reglas
        cacheService.invalidate(rule.getVehicleId());
        
        log.info("Rule created successfully: ID={}", saved.getId());
        
        return ruleMapper.toDTO(saved);
    }
    
    @Override
    public RuleDTO updateRule(Long id, RuleDTO ruleDTO) {
        log.info("Updating rule: ID={}", id);
        
        ruleDTO.setId(id);
        Rule rule = ruleMapper.toDomain(ruleDTO);
        Rule updated = ruleRepository.save(rule);
        
        cacheService.invalidate(rule.getVehicleId());
        
        return ruleMapper.toDTO(updated);
    }
    
    @Override
    public void deleteRule(Long id) {
        log.info("Deleting rule: ID={}", id);
        
        Rule rule = ruleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rule not found: " + id));
        
        ruleRepository.deleteById(id);
        cacheService.invalidate(rule.getVehicleId());
    }
    
    @Override
    public RuleDTO getRule(Long id) {
        return ruleRepository.findById(id)
            .map(ruleMapper::toDTO)
            .orElseThrow(() -> new RuntimeException("Rule not found: " + id));
    }
}