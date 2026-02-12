\# CCS Monitoring API



Sistema central de monitoreo para la Compañía Colombiana de Seguimiento (CCS), diseñado bajo Arquitectura Hexagonal (Ports \& Adapters), orientado a procesamiento de señales masivas, eventos de emergencia en tiempo real y reglas configurables por propietario.



El sistema soporta:

\- Procesamiento de hasta 500 señales por segundo

\- Ejecución de acciones de emergencia en menos de 2 segundos

\- Escalabilidad horizontal

\- Separación OLTP / OLAP

\- Procesamiento asíncrono basado en eventos



---



\# 1. Arquitectura



Se implementa Arquitectura Hexagonal (Clean Architecture + DDD).



\## Capas



\### Domain

\- Entidades: Vehicle, Signal, Rule, Alert, EmergencyEvent

\- Value Objects: VehicleId, Coordinates, SignalType, RuleCondition, AlertLevel

\- Servicios de Dominio: RuleEngine

\- Puertos (Ports):

&nbsp; - Input: ProcessSignalUseCase, HandleEmergencyUseCase, ConfigureRuleUseCase

&nbsp; - Output: VehicleRepository, SignalRepository, RuleRepository, AlertRepository, NotificationService

\- Sin dependencias de frameworks



\### Application

\- Orquesta casos de uso

\- Contiene DTOs y mappers

\- Implementa lógica transaccional

\- Servicios:

&nbsp; - ProcessSignalService

&nbsp; - HandleEmergencyService

&nbsp; - ConfigureRuleService



\### Infrastructure

\- REST Controllers

\- RabbitMQ Listeners

\- JPA Repositories

\- Redis Cache Adapter

\- Configuración Spring Boot

\- Persistencia PostgreSQL + TimescaleDB



---



\# 2. Stack Tecnológico



\- Java 17

\- Spring Boot 3

\- Spring Web

\- Spring Data JPA

\- PostgreSQL

\- TimescaleDB

\- Redis

\- RabbitMQ

\- Flyway

\- Docker

\- Maven



---



\# 3. Esquema de Base de Datos



Separación por esquemas:



\## operational (OLTP)

\- vehicles

\- rules

\- alerts



Optimizado para:

\- Transacciones

\- Estado actual

\- Dashboard en tiempo real



\## telemetry (OLAP masivo)

\- vehicle\_signals (Hypertable)

\- signals\_hourly (Continuous Aggregate)



Optimizado para:

\- Escritura masiva

\- Consultas históricas

\- Compresión automática

\- Retención 180 días



---



\# 4. Modelo Entidad-Relación



Relaciones principales:



\- vehicles 1:N rules

\- rules 1:N alerts

\- vehicles 1:N alerts

\- vehicles 1:N vehicle\_signals



vehicle\_signals es hypertable particionada por tiempo.



---



\# 5. Instalación del Entorno



\## Requisitos



\- Java 17

\- Maven 3.9+

\- Docker y Docker Compose



\## Clonar proyecto



```bash

git clone <repository>

cd hexagonal-users-api-mejorada



