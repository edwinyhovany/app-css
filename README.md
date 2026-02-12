
---

# 📄 README-ARQUITECTURA

```markdown
# 🏗️ Documentación de Arquitectura – CCS Monitoring

Documento técnico que describe la arquitectura lógica y técnica de la central de monitoreo CCS.

---

## 🎯 Objetivos Arquitectónicos

- Procesar 500 señales por segundo
- Ejecutar acciones de emergencia en menos de 2 segundos
- Soportar crecimiento anual del 20%
- Alta disponibilidad
- Escalabilidad horizontal
- Separación OLTP / OLAP

---

# 🔴 CAPA DOMAIN

Lógica pura del negocio, sin dependencias de frameworks.

## Entidades
- Vehicle
- Signal
- Rule
- Alert
- EmergencyEvent

## Value Objects
- VehicleId
- Coordinates
- SignalType
- RuleCondition
- AlertLevel

## Servicios de Dominio
- RuleEngine
- EmergencyDetector

## Puertos

### Input Ports
- ProcessSignalUseCase
- HandleEmergencyUseCase
- ConfigureRuleUseCase

### Output Ports
- VehicleRepository
- SignalRepository
- RuleRepository
- AlertRepository
- NotificationService

---

# 🔵 CAPA APPLICATION

Orquesta los casos de uso.

## Servicios
- ProcessSignalService
- HandleEmergencyService
- ConfigureRuleService

Responsabilidades:
- Control transaccional
- Validación de DTOs
- Conversión DTO ↔ Dominio
- Coordinación de lógica

---

# 🟢 CAPA INFRASTRUCTURE

Implementación técnica de los puertos.

Incluye:
- REST Controllers
- RabbitMQ Listeners
- JPA Repositories
- Redis Cache Adapter
- Configuración Spring
- Migraciones Flyway

---

# 📊 Modelo de Base de Datos

## Esquema operational (OLTP)
- vehicles
- rules
- alerts

## Esquema telemetry (OLAP)
- vehicle_signals (Hypertable)
- signals_hourly

Separación diseñada para evitar:
- Lock contention
- Saturación por consultas analíticas

---

# 📬 Arquitectura de Mensajería

## RabbitMQ con 3 Colas

### Emergency Queue
- Workers dedicados
- Garantiza SLA < 2s

### Signals Queue
- Procesamiento masivo
- Batch optimizado

### Notifications Queue
- Prioridad baja

Permite:
- Escalado independiente
- Aislamiento de fallos

---

# ⚙️ Estrategia de Performance

## Dual DataSource
- Pool OLTP: 20 conexiones
- Pool Telemetría: 50 conexiones

## Redis Multi-Nivel
- L1: Memoria local
- L2: Redis

## TimescaleDB
- Particionamiento diario
- Compresión automática
- Retención 180 días

---

# 📈 Modelo de Escalabilidad

Sistema stateless.

Escalado:
- Agregando pods
- Aumentando consumidores RabbitMQ
- Añadiendo réplicas de lectura

Escala de:
- 500 señales/s
hasta:
- 5,000+ señales/s

---

# 🛡️ Modelo de Disponibilidad

- Multi-AZ
- Failover automático en RDS
- Redis Cluster
- RabbitMQ Cluster
- Dead Letter Queue
- Circuit Breaker

No existe punto único de falla.

---

# 🧠 Patrones Aplicados

- Arquitectura Hexagonal
- Domain-Driven Design
- Event-Driven Architecture
- CQRS ligero
- Cache-Aside
- Bulkhead Pattern
- Circuit Breaker
- Horizontal Scaling

---

# 🚀 Conclusión

La solución está diseñada para soportar alta concurrencia, garantizar tiempos de respuesta críticos en emergencias y permitir crecimiento sostenido sin rediseños estructurales.
