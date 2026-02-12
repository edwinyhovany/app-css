
---

# README Documento Arquitectónico

```markdown
# 🏗️ CCS Monitoring – Architecture Documentation

Technical architecture document for the CCS vehicle monitoring central platform.

---

## 🎯 Architectural Objectives

- Process 500 signals per second
- Emergency response under 2 seconds
- 20% annual growth scalability
- High availability
- Zero shared state
- OLTP / OLAP separation

---

# 🔴 DOMAIN LAYER

Pure business logic. No framework dependencies.

## Entities
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

## Domain Services
- RuleEngine
- EmergencyDetector

## Ports

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

# 🔵 APPLICATION LAYER

Coordinates domain logic and transactions.

## Services
- ProcessSignalService
- HandleEmergencyService
- ConfigureRuleService

## Responsibilities
- Transaction control
- DTO validation
- Mapping DTO ↔ Domain
- Use case orchestration

---

# 🟢 INFRASTRUCTURE LAYER

Implements external concerns.

- REST Controllers
- RabbitMQ Listeners
- JPA Adapters
- Redis Cache Adapter
- Flyway Migrations
- Spring Configuration

---

# 📊 Database Design

## 1️⃣ Schema Separation

operational (OLTP)
- vehicles
- rules
- alerts

telemetry (OLAP)
- vehicle_signals (Hypertable)
- signals_hourly (Continuous Aggregate)

Prevents:
- Lock contention
- Query degradation

---

# 📬 Messaging Architecture

## RabbitMQ Queues

Emergency Queue
- Dedicated workers
- Guaranteed <2s SLA

Signals Queue
- High throughput processing
- Batch optimized

Notifications Queue
- Lower priority

Enables:
- Independent scaling
- Fault isolation

---

# ⚙️ Performance Strategy

## Dual DataSource
- OLTP pool: 20 connections
- Telemetry pool: 50 connections

## Redis Multi-Level Cache
- L1: In-memory
- L2: Redis

## Time-Series Optimization
- Daily partitioning
- Compression after 7 days
- 180-day retention

---

# 📈 Scalability Model

Stateless services.

Scaling approach:
- Add application pods
- Increase RabbitMQ consumers
- Scale database vertically or add read replicas

From:
- 500 signals/sec
To:
- 5,000+ signals/sec

---

# 🛡️ Availability Model

- Multi-AZ deployment
- RDS automatic failover
- Redis cluster
- RabbitMQ cluster
- Dead Letter Queues
- Circuit breaker pattern for external integrations

No single point of failure.

---

# 🧠 Architectural Patterns Applied

- Hexagonal Architecture
- Domain-Driven Design
- Event-Driven Architecture
- CQRS (lightweight)
- Cache-Aside Pattern
- Bulkhead Pattern
- Circuit Breaker Pattern
- Stateless Services
- Horizontal Scaling

---

# 🚀 Production Recommendation

- Deploy on Kubernetes (EKS)
- Enable HPA
- Enable metrics with Prometheus
- Centralized logging
- Distributed tracing

---

# 📌 Conclusion

The system is designed for high-throughput telemetric ingestion with strict emergency SLAs, supporting independent scaling of components while maintaining strong domain integrity and separation of concerns.

