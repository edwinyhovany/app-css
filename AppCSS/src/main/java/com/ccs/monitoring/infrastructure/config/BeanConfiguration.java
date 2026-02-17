package com.ccs.monitoring.infrastructure.config;

import com.ccs.monitoring.application.mapper.RuleMapper;
import com.ccs.monitoring.application.service.ConfigureRuleService;
import com.ccs.monitoring.domain.port.in.ConfigureRuleUseCase;
import com.ccs.monitoring.domain.port.out.CacheService;
import com.ccs.monitoring.domain.port.out.RuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    
    @Bean
    public ConfigureRuleUseCase configureRuleUseCase(
            RuleRepository ruleRepository,
            CacheService cacheService,
            RuleMapper ruleMapper) {
        return new ConfigureRuleService(ruleRepository, cacheService, ruleMapper);
    }
}