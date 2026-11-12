-- Extensiones (siempre en public)
CREATE EXTENSION IF NOT EXISTS timescaledb SCHEMA public;
CREATE EXTENSION IF NOT EXISTS pg_trgm SCHEMA public;

-- Esquema para datos operacionales (OLTP)
CREATE SCHEMA IF NOT EXISTS operational;

-- Esquema para datos de telemetría (OLAP)
CREATE SCHEMA IF NOT EXISTS telemetry;

-- Comentarios para documentación
COMMENT ON SCHEMA operational IS 'Esquema para datos transaccionales: vehículos, reglas, alertas';
COMMENT ON SCHEMA telemetry IS 'Esquema para datos de telemetría masiva: señales de vehículos';