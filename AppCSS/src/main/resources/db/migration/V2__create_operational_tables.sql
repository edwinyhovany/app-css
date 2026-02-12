-- ==============================================
-- ESQUEMA OPERATIONAL - Tablas OLTP
-- ==============================================

-- Tabla de vehículos
CREATE TABLE operational.vehicles (
    id VARCHAR(50) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type VARCHAR(20) NOT NULL,
    owner_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    last_signal_time TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_vehicle_type CHECK (vehicle_type IN ('TRUCK', 'BUS', 'TAXI', 'MOTORCYCLE', 'PRIVATE_CAR')),
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE'))
);

-- Índices para búsquedas frecuentes
CREATE INDEX idx_vehicles_owner ON operational.vehicles(owner_id);
CREATE INDEX idx_vehicles_status ON operational.vehicles(status);
CREATE INDEX idx_vehicles_last_signal ON operational.vehicles(last_signal_time DESC);

-- Tabla de reglas configurables
CREATE TABLE operational.rules (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id VARCHAR(50) NOT NULL REFERENCES operational.vehicles(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    condition_type VARCHAR(50) NOT NULL,
    condition_parameter VARCHAR(100),
    condition_operator VARCHAR(10),
    condition_value VARCHAR(100),
    actions JSONB NOT NULL DEFAULT '[]'::jsonb,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    last_executed_at TIMESTAMPTZ,
    
    CONSTRAINT chk_condition_type CHECK (condition_type IN ('THRESHOLD', 'TIME_BASED', 'GEOFENCE', 'EMERGENCY'))
);

-- Índices para reglas
CREATE INDEX idx_rules_vehicle_active ON operational.rules(vehicle_id, active) WHERE active = TRUE;
CREATE INDEX idx_rules_condition_type ON operational.rules(condition_type);

-- Tabla de alertas generadas
CREATE TABLE operational.alerts (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id VARCHAR(50) NOT NULL,
    signal_id BIGINT,
    rule_id BIGINT REFERENCES operational.rules(id) ON DELETE SET NULL,
    rule_name VARCHAR(100) NOT NULL,
    level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    resolved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMPTZ,
    
    CONSTRAINT chk_alert_level CHECK (level IN ('INFO', 'WARNING', 'CRITICAL'))
);

-- Índices para alertas
CREATE INDEX idx_alerts_vehicle ON operational.alerts(vehicle_id);
CREATE INDEX idx_alerts_created ON operational.alerts(created_at DESC);
CREATE INDEX idx_alerts_level ON operational.alerts(level) WHERE resolved = FALSE;
CREATE INDEX idx_alerts_unresolved ON operational.alerts(resolved, created_at DESC) WHERE resolved = FALSE;

-- Comentarios para documentación
COMMENT ON TABLE operational.vehicles IS 'Catálogo maestro de vehículos monitoreados';
COMMENT ON TABLE operational.rules IS 'Reglas configurables por propietarios de vehículos';
COMMENT ON TABLE operational.alerts IS 'Alertas generadas por evaluación de reglas (últimos 90 días)';