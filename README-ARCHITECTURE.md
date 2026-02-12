

---



\# README-ARCHITECTURE.md (Documento Externo)



```markdown

\# CCS Monitoring - Documento de Arquitectura



Este documento describe la arquitectura lógica y técnica de la solución CCS Monitoring.



---



\# 1. Arquitectura General



Se adopta Arquitectura Hexagonal con principios DDD y Event-Driven Architecture.



Objetivos:



\- Procesar 500 señales/seg

\- Emergencias < 2 segundos

\- Escalabilidad horizontal

\- Alta disponibilidad

\- Separación OLTP / OLAP



---



\# 2. Capas de la Solución



\## CAPA DOMAIN



Responsable de la lógica pura de negocio.



\### Entidades

\- Vehicle

\- Signal

\- Rule

\- Alert

\- EmergencyEvent



\### Value Objects

\- VehicleId

\- Coordinates

\- SignalType

\- RuleCondition



\### Servicios de Dominio

\- RuleEngine

\- EmergencyDetector



\### Puertos



Input Ports:

\- ProcessSignalUseCase

\- HandleEmergencyUseCase

\- ConfigureRuleUseCase



Output Ports:

\- VehicleRepository

\- SignalRepository

\- RuleRepository

\- AlertRepository

\- NotificationService



Sin dependencias de frameworks.



---



\## CAPA APPLICATION



Orquesta los casos de uso.



\- ProcessSignalService

\- HandleEmergencyService

\- ConfigureRuleService



Incluye:

\- DTOs con validación

\- Mappers

\- Control transaccional



---



\## CAPA INFRASTRUCTURE



Implementaciones concretas de los puertos:



\- REST Controllers

\- RabbitMQ Listeners

\- JPA Repositories

\- Redis Cache Adapter

\- Configuración Spring

\- Flyway migrations



---



\# 3. Estrategia de Escalabilidad



\## 1. BD Separada por Esquemas



operational: OLTP  

telemetry: Time-series



Soluciona:

\- Lock contention

\- Degradación de queries



\## 2. RabbitMQ con 3 Colas



Emergency:

\- 5 workers

\- SLA < 2s



Signals:

\- 8 workers

\- Batch processing



Notifications:

\- 2 workers



Escalado independiente por tipo.



\## 3. Dual DataSource



Pool operacional: 20 conexiones  

Pool telemetría: 50 conexiones  



Evita contention del pool.



\## 4. Cache Redis Multi-Nivel



L1: In-memory  

L2: Redis  



Reduce acceso a BD.



---



\# 4. Disponibilidad



\- Arquitectura stateless

\- Workers independientes

\- DLQ configurada

\- Multi-AZ en producción

\- RDS con failover

\- Redis cluster

\- RabbitMQ cluster



No existe punto único de falla.



---



\# 5. Modelo de Datos



Separación clara:



OLTP:

\- vehicles

\- rules

\- alerts



OLAP:

\- vehicle\_signals (hypertable)

\- signals\_hourly (aggregate)



Retención automática 180 días.

Compresión automática después de 7 días.



---



\# 6. Principios Aplicados



\- Hexagonal Architecture

\- Domain-Driven Design

\- Event-Driven Architecture

\- CQRS ligero

\- Cache-aside

\- Bulkhead Pattern

\- Stateless services

\- Horizontal scaling



---



\# 7. Escenario de Crecimiento



Soporta:



\- De 500 a 5,000 señales/s

\- Crecimiento anual 20%

\- Escalado agregando workers y pods

\- Sin rediseño estructural



---



\# 8. Garantía SLA Emergencias



\- Cola priorizada

\- Workers dedicados

\- Persistencia inmediata

\- Notificación asíncrona

\- Circuit breaker en integraciones externas



