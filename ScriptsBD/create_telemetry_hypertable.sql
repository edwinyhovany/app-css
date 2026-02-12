-- ==============================================
-- ESQUEMA TELEMETRY - Hypertable para Time-Series
-- ==============================================

-- Tabla de señales de telemetría
CREATE TABLE telemetry.vehicle_signals (
    id BIGSERIAL,
    vehicle_id VARCHAR(50) NOT NULL,
    signal_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    speed DECIMAL(5, 2),
    direction DECIMAL(5, 2),
    temperature DECIMAL(5, 2),
    cargo_status VARCHAR(50),
    emergency BOOLEAN DEFAULT FALSE,
    metadata JSONB,
    
    PRIMARY KEY (id, timestamp),
    
    CONSTRAINT chk_signal_type CHECK (signal_type IN ('NORMAL', 'EMERGENCY', 'PANIC_BUTTON', 'MAINTENANCE')),
    CONSTRAINT chk_latitude CHECK (latitude >= -90 AND latitude <= 90),
    CONSTRAINT chk_longitude CHECK (longitude >= -180 AND longitude <= 180),
    CONSTRAINT chk_speed CHECK (speed >= 0)
);

-- Convertir a hypertable (particionamiento automático por tiempo)
SELECT create_hypertable(
    'telemetry.vehicle_signals', 
    'timestamp',
    chunk_time_interval => INTERVAL '1 day',
    if_not_exists => TRUE
);

-- Índice para emergencias (partial index - solo emergencias)
CREATE INDEX idx_signals_emergency 
    ON telemetry.vehicle_signals(timestamp DESC) 
    WHERE emergency = TRUE;

-- Índice para vehículo + tiempo (búsquedas frecuentes)
CREATE INDEX idx_signals_vehicle_time 
    ON telemetry.vehicle_signals(vehicle_id, timestamp DESC);

-- Índice para tipo de señal
CREATE INDEX idx_signals_type 
    ON telemetry.vehicle_signals(signal_type, timestamp DESC);

-- Configurar compresión automática (después de 7 días)
ALTER TABLE telemetry.vehicle_signals 
    SET (
        timescaledb.compress,
        timescaledb.compress_segmentby = 'vehicle_id',
        timescaledb.compress_orderby = 'timestamp DESC'
    );

-- Política de compresión: comprimir chunks de más de 7 días
SELECT add_compression_policy(
    'telemetry.vehicle_signals', 
    INTERVAL '7 days',
    if_not_exists => TRUE
);

-- Política de retención: eliminar datos de más de 180 días
SELECT add_retention_policy(
    'telemetry.vehicle_signals', 
    INTERVAL '180 days',
    if_not_exists => TRUE
);

-- Continuous aggregate para métricas por hora (opcional pero útil)
CREATE MATERIALIZED VIEW telemetry.signals_hourly
WITH (timescaledb.continuous) AS
SELECT 
    vehicle_id,
    time_bucket('1 hour', timestamp) AS hour,
    COUNT(*) as signal_count,
    AVG(speed) as avg_speed,
    MAX(speed) as max_speed,
    COUNT(*) FILTER (WHERE emergency = TRUE) as emergency_count
FROM telemetry.vehicle_signals
GROUP BY vehicle_id, hour
WITH NO DATA;

-- Política de refresh para la vista materializada
SELECT add_continuous_aggregate_policy(
    'telemetry.signals_hourly',
    start_offset => INTERVAL '3 hours',
    end_offset => INTERVAL '1 hour',
    schedule_interval => INTERVAL '1 hour',
    if_not_exists => TRUE
);

-- Comentarios para documentación
COMMENT ON TABLE telemetry.vehicle_signals IS 'Hypertable de señales de telemetría - particionada por día, compresión automática después de 7 días, retención 180 días';
COMMENT ON MATERIALIZED VIEW telemetry.signals_hourly IS 'Vista materializada con agregaciones por hora para queries analíticas rápidas';