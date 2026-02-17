package com.ccs.monitoring.infrastructure.adapter.outbound.persistence.cache;

import com.ccs.monitoring.domain.model.Rule;
import com.ccs.monitoring.domain.port.out.CacheService;
import com.ccs.monitoring.domain.valueobject.VehicleId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RedisCacheAdapter implements CacheService {
    
    private static final Logger log = LoggerFactory.getLogger(RedisCacheAdapter.class);
    private static final String CACHE_PREFIX = "rules:vehicle:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    
    // L1 Cache - In-memory (más rápido)
    private final Map<String, List<Rule>> l1Cache = new ConcurrentHashMap<>();
    
    // L2 Cache - Redis (compartido entre instancias)
    private final RedisTemplate<String, Object> redisTemplate;
    
    public RedisCacheAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public List<Rule> getRules(VehicleId vehicleId) {
        String key = vehicleId.getValue();
        
        // 1. Buscar en L1 (in-memory)
        List<Rule> rules = l1Cache.get(key);
        if (rules != null) {
            log.debug("L1 Cache HIT for vehicle: {}", vehicleId);
            return rules;
        }
        
        // 2. Buscar en L2 (Redis)
        String redisKey = CACHE_PREFIX + key;
        try {
            @SuppressWarnings("unchecked")
            List<Rule> cachedRules = (List<Rule>) redisTemplate.opsForValue().get(redisKey);
            
            if (cachedRules != null) {
                log.debug("L2 Cache HIT for vehicle: {}", vehicleId);
                // Guardar en L1 para siguiente acceso
                l1Cache.put(key, cachedRules);
                return cachedRules;
            }
        } catch (Exception e) {
            log.warn("Error reading from Redis cache: {}", e.getMessage());
        }
        
        log.debug("Cache MISS for vehicle: {}", vehicleId);
        return null;
    }
    
    @Override
    public void putRules(VehicleId vehicleId, List<Rule> rules) {
        String key = vehicleId.getValue();
        
        // Guardar en L1
        l1Cache.put(key, rules);
        
        // Guardar en L2 con TTL
        String redisKey = CACHE_PREFIX + key;
        try {
            redisTemplate.opsForValue().set(redisKey, rules, CACHE_TTL);
            log.debug("Rules cached for vehicle: {} (TTL: {})", vehicleId, CACHE_TTL);
        } catch (Exception e) {
            log.warn("Error writing to Redis cache: {}", e.getMessage());
        }
    }
    
    @Override
    public void invalidate(VehicleId vehicleId) {
        String key = vehicleId.getValue();
        
        // Invalidar L1
        l1Cache.remove(key);
        
        // Invalidar L2
        String redisKey = CACHE_PREFIX + key;
        try {
            redisTemplate.delete(redisKey);
            log.debug("Cache invalidated for vehicle: {}", vehicleId);
        } catch (Exception e) {
            log.warn("Error invalidating Redis cache: {}", e.getMessage());
        }
    }
    
    @Override
    public void clearAll() {
        l1Cache.clear();
        log.info("L1 Cache cleared");
        // Redis se limpia por TTL automático
    }
}
