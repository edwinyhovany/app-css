-- ==============================================
-- ÍNDICES ADICIONALES PARA PERFORMANCE
-- ==============================================

-- Índice para búsquedas por placa
CREATE INDEX idx_vehicles_license_plate_trgm 
    ON operational.vehicles USING gin (license_plate public.gin_trgm_ops);

-- Índice para owner_id + status (query frecuente: "mis vehículos activos")
CREATE INDEX idx_vehicles_owner_status 
    ON operational.vehicles(owner_id, status) 
    WHERE status = 'ACTIVE';

-- Índice compuesto para reglas activas por vehículo
CREATE INDEX idx_rules_vehicle_active_composite 
    ON operational.rules(vehicle_id, active, condition_type) 
    WHERE active = TRUE;

-- Índice para alertas críticas no resueltas (dashboard)
CREATE INDEX idx_alerts_critical_unresolved 
    ON operational.alerts(created_at DESC) 
    WHERE level = 'CRITICAL' AND resolved = FALSE;

-- Índice GIN para búsquedas en metadata JSON
CREATE INDEX idx_signals_metadata_gin 
ON telemetry.vehicle_signals 
USING gin (metadata jsonb_path_ops);

-- Estadísticas para el optimizador
ANALYZE operational.vehicles;
ANALYZE operational.rules;
ANALYZE operational.alerts;
ANALYZE telemetry.vehicle_signals;