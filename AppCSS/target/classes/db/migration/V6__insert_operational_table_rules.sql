-- ============================================
-- Reglas de Prueba
-- ============================================

-- Regla 1: Velocidad máxima para camiones (>90 km/h)
INSERT INTO operational.rules (vehicle_id, name, condition_type, condition_parameter, condition_operator, condition_value, actions, active) VALUES
('VEH001', 'Velocidad Máxima Camión', 'THRESHOLD', 'speed', '>', '90', '["SEND_SMS", "SEND_EMAIL"]', true),
('VEH005', 'Velocidad Máxima Camión', 'THRESHOLD', 'speed', '>', '90', '["SEND_SMS", "SEND_EMAIL"]', true),
('VEH008', 'Velocidad Máxima Camión', 'THRESHOLD', 'speed', '>', '90', '["SEND_SMS", "SEND_EMAIL"]', true)
ON CONFLICT DO NOTHING;

-- Regla 2: Temperatura de carga refrigerada (>5°C)
INSERT INTO operational.rules (vehicle_id, name, condition_type, condition_parameter, condition_operator, condition_value, actions, active) VALUES
('VEH001', 'Temperatura Carga Fría', 'THRESHOLD', 'temperature', '>', '5', '["SEND_SMS", "TRIGGER_ALARM"]', true),
('VEH005', 'Temperatura Carga Fría', 'THRESHOLD', 'temperature', '>', '5', '["SEND_SMS", "TRIGGER_ALARM"]', true),
('VEH008', 'Temperatura Carga Fría', 'THRESHOLD', 'temperature', '>', '5', '["SEND_SMS", "TRIGGER_ALARM"]', true)
ON CONFLICT DO NOTHING;

-- Regla 3: Detección de emergencias (todos los vehículos)
INSERT INTO operational.rules (vehicle_id, name, condition_type, condition_parameter, condition_operator, condition_value, actions, active) VALUES
('VEH001', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH002', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH003', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH004', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH005', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH006', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH007', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH008', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH009', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true),
('VEH010', 'Botón de Pánico', 'EMERGENCY', null, null, null, '["CALL_AUTHORITIES", "SEND_SMS", "NOTIFY_OWNER"]', true)
ON CONFLICT DO NOTHING;

-- Regla 4: Velocidad máxima para taxis (>80 km/h)
INSERT INTO operational.rules (vehicle_id, name, condition_type, condition_parameter, condition_operator, condition_value, actions, active) VALUES
('VEH003', 'Velocidad Máxima Taxi', 'THRESHOLD', 'speed', '>', '80', '["SEND_SMS"]', true),
('VEH007', 'Velocidad Máxima Taxi', 'THRESHOLD', 'speed', '>', '80', '["SEND_SMS"]', true)
ON CONFLICT DO NOTHING;