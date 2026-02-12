# 🚛 Plataforma Monitoreo Vehiculos

Plataforma central de monitoreo de vehículos para la Compañía Colombiana de Seguimiento (CCS), diseñada bajo Arquitectura Hexagonal, Domain-Driven Design y arquitectura orientada a eventos.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![TimescaleDB](https://img.shields.io/badge/TimescaleDB-Time--Series-9cf)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Event--Driven-ff6600)
![Redis](https://img.shields.io/badge/Redis-Cache-red)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Hexagonal-blueviolet)

---

## 🎯 Características Principales

### Núcleo del Sistema
- ✅ Procesamiento de hasta **500 señales por segundo**
- 🚨 Gestión de emergencias en **menos de 2 segundos**
- ⚡ Arquitectura orientada a eventos (Event-Driven)
- 🧠 Motor de reglas configurable por propietario
- 📊 Base de datos optimizada para series de tiempo (TimescaleDB)
- 🔄 Procesamiento asíncrono con RabbitMQ
- 🗄️ Separación OLTP / OLAP
- 🚀 Escalabilidad horizontal
- 🛡️ Alta disponibilidad sin punto único de falla

---

## 🏗️ Arquitectura

El proyecto está diseñado bajo Arquitectura Hexagonal (Ports & Adapters) combinada con Domain-Driven Design (DDD) y principios de Event-Driven Architecture, garantizando:

- Bajo acoplamiento
- Alta cohesión
- Escalabilidad
- Testabilidad
- Independencia de frameworks
  
# 📦 Estructura del Proyecto

A continuación se presenta la estructura completa del proyecto basada en **Arquitectura Hexagonal + DDD + Event-Driven Architecture**.

---

# 🗂️ Estructura General
```
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── ccs/
│ │ │ └── monitoring/
│ │ │
│ │ │ ├── domain/ -- 🔴 CAPA DOMAIN (Núcleo puro) 
│ │ │ │ ├── model/
│ │ │ │ │ ├── Vehicle.java
│ │ │ │ │ ├── Signal.java
│ │ │ │ │ ├── Rule.java
│ │ │ │ │ ├── Alert.java
│ │ │ │ │ └── EmergencyEvent.java
│ │ │ │ │
│ │ │ │ ├── valueobject/
│ │ │ │ │ ├── VehicleId.java
│ │ │ │ │ ├── SignalType.java
│ │ │ │ │ ├── Coordinates.java
│ │ │ │ │ ├── RuleCondition.java
│ │ │ │ │ └── AlertLevel.java
│ │ │ │ │
│ │ │ │ ├── service/
│ │ │ │ │ ├── RuleEngine.java
│ │ │ │ │ └── EmergencyDetector.java
│ │ │ │ │
│ │ │ │ └── port/ # Puertos (interfaces)
│ │ │ │ ├── in/ # Input Ports (Use Cases)
│ │ │ │ │ ├── ProcessSignalUseCase.java
│ │ │ │ │ ├── EvaluateRulesUseCase.java
│ │ │ │ │ ├── HandleEmergencyUseCase.java
│ │ │ │ │ ├── ConfigureRuleUseCase.java
│ │ │ │ │ └── GetVehicleStatusUseCase.java
│ │ │ │ │
│ │ │ │ └── out/ # Output Ports (Repositories / External Services)
│ │ │ │ ├── VehicleRepository.java
│ │ │ │ ├── SignalRepository.java
│ │ │ │ ├── RuleRepository.java
│ │ │ │ ├── AlertRepository.java
│ │ │ │ ├── NotificationService.java
│ │ │ │ └── CacheService.java
│ │ │
│ │ │ ├── application/ -- 🔵 CAPA APPLICATION (Casos de Uso) 
│ │ │ │ ├── service/
│ │ │ │ │ ├── ProcessSignalService.java
│ │ │ │ │ ├── EvaluateRulesService.java
│ │ │ │ │ ├── HandleEmergencyService.java
│ │ │ │ │ ├── ConfigureRuleService.java
│ │ │ │ │ └── GetVehicleStatusService.java
│ │ │ │ │
│ │ │ │ ├── dto/
│ │ │ │ │ ├── SignalDTO.java
│ │ │ │ │ ├── VehicleDTO.java
│ │ │ │ │ ├── RuleDTO.java
│ │ │ │ │ ├── AlertDTO.java
│ │ │ │ │ └── EmergencyDTO.java
│ │ │ │ │
│ │ │ │ └── mapper/
│ │ │ │ ├── SignalMapper.java
│ │ │ │ ├── VehicleMapper.java
│ │ │ │ ├── RuleMapper.java
│ │ │ │ └── AlertMapper.java
│ │ │
│ │ │ └── infrastructure/ -- 🟢 CAPA INFRASTRUCTURE (Adaptadores) 
│ │ │ ├── adapter/
│ │ │ │ ├── in/ # Adaptadores de entrada
│ │ │ │ │ ├── rest/
│ │ │ │ │ │ ├── SignalController.java
│ │ │ │ │ │ ├── VehicleController.java
│ │ │ │ │ │ ├── RuleController.java
│ │ │ │ │ │ ├── AlertController.java
│ │ │ │ │ │ └── EmergencyController.java
│ │ │ │ │ │
│ │ │ │ │ └── messaging/
│ │ │ │ │ ├── RabbitMQSignalListener.java
│ │ │ │ │ └── RabbitMQConfig.java
│ │ │ │ │
│ │ │ │ └── out/ # Adaptadores de salida
│ │ │ │ ├── persistence/
│ │ │ │ │ ├── VehicleJpaRepository.java
│ │ │ │ │ ├── SignalJpaRepository.java
│ │ │ │ │ ├── RuleJpaRepository.java
│ │ │ │ │ ├── AlertJpaRepository.java
│ │ │ │ │ ├── VehiclePersistenceAdapter.java
│ │ │ │ │ ├── SignalPersistenceAdapter.java
│ │ │ │ │ ├── RulePersistenceAdapter.java
│ │ │ │ │ └── AlertPersistenceAdapter.java
│ │ │ │ │
│ │ │ │ ├── entity/
│ │ │ │ │ ├── VehicleEntity.java
│ │ │ │ │ ├── SignalEntity.java
│ │ │ │ │ ├── RuleEntity.java
│ │ │ │ │ └── AlertEntity.java
│ │ │ │ │
│ │ │ │ ├── cache/
│ │ │ │ │ └── RedisCacheAdapter.java
│ │ │ │ │
│ │ │ │ └── notification/
│ │ │ │ ├── RabbitMQNotificationAdapter.java
│ │ │ │ └── ExternalAPINotificationAdapter.java
│ │ │ │
│ │ │ └── config/
│ │ │ ├── DatabaseConfig.java
│ │ │ ├── RabbitMQConfiguration.java
│ │ │ ├── RedisConfiguration.java
│ │ │ ├── OpenAPIConfiguration.java
│ │ │ └── BeanConfiguration.java
│ │
│ │ └── resources/
│ │ ├── application.yml
│ │ ├── application-dev.yml
│ │ ├── application-prod.yml
│ │ └── db/
│ │ └── migration/
│ │ ├── V1__create_vehicles_table.sql
│ │ ├── V2__create_signals_hypertable.sql
│ │ ├── V3__create_rules_table.sql
│ │ └── V4__create_alerts_table.sql
│ │
│ └── test/
│ └── java/
│ └── com/
│ └── ccs/
│ └── monitoring/
│
│ ├── domain/ # Tests de dominio (sin mocks)
│ │ ├── model/
│ │ │ ├── VehicleTest.java
│ │ │ ├── SignalTest.java
│ │ │ └── RuleTest.java
│ │ │
│ │ └── service/
│ │ ├── RuleEngineTest.java
│ │ └── EmergencyDetectorTest.java
│ │
│ ├── application/ # Tests de casos de uso
│ │ └── service/
│ │ ├── ProcessSignalServiceTest.java
│ │ ├── EvaluateRulesServiceTest.java
│ │ └── HandleEmergencyServiceTest.java
│ │
│ └── infrastructure/ # Tests de integración
│ └── adapter/
│ ├── rest/
│ │ ├── SignalControllerTest.java
│ │ └── VehicleControllerTest.java
│ │
│ └── persistence/
│ ├── SignalPersistenceAdapterTest.java
│ └── VehiclePersistenceAdapterTest.java
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml (o build.gradle)
└── README.md
```

---

### Capas de Arquitectura

## 🔴 CAPA DOMAIN (Núcleo del Negocio)
Es el corazón del sistema. No depende de frameworks, bases de datos ni infraestructura externa.

## 🔵 CAPA APPLICATION (Orquestación)

Implementa los casos de uso definidos en el dominio, coordina transacciones, validaciones, conversión DTO ↔ Dominio, llamadas a puertos de salida, no contiene reglas de negocio puras, solo orquestación.

## 🟢 CAPA INFRASTRUCTURE (Adaptadores)

Implementa los contratos definidos por el dominio.Aquí viven los frameworks y dependencias externas.

## 🧪 CAPA DE TESTING

La estrategia de testing sigue la pirámide de pruebas:

🔹 Domain Tests

- Sin mocks
- Prueban reglas puras

🔹 Application Tests

- Mockean puertos de salida
- Validan casos de uso

🔹 Infrastructure Tests

- Tests de integración
- Validan controladores y persistencia
---

## 🚀 Stack Tecnológico

### Backend
- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3
- **Persistencia:** Spring Data JPA
- **Base de Datos:** PostgreSQL + TimescaleDB
- **Mensajería:** RabbitMQ
- **Cache:** Redis (multi-nivel)
- **Migraciones:** Flyway
- **Build Tool:** Maven
- **Contenedores:** Docker

---

## 📊 Diseño de Base de Datos

### Esquema `operational` (OLTP)
- vehicles
- rules
- alerts

Optimizado para:
- Transacciones rápidas
- Estado actual del sistema
- Dashboards operativos

### Esquema `telemetry` (OLAP)
- vehicle_signals (Hypertable)
- signals_hourly (Vista agregada)

Optimizado para:
- Escritura masiva
- Consultas históricas
- Compresión automática
- Retención de 180 días

---

## 📦 Instalación

### Requisitos
- Java 21
- Maven 3.9+
- Docker y Docker Compose

### Clonar & Instalar
```bash
git clone (https://github.com/edwinyhovany/app-css.git)
cd AppCSS
```

### Variables de Entorno

Configurar en **application.yml** o variables del sistema: :
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ccs
SPRING_DATASOURCE_USERNAME=ccs
SPRING_DATASOURCE_PASSWORD=ccs

REDIS_HOST=localhost
REDIS_PORT=6379

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

```

### 🐳 Levantar Infraestructura
```
docker-compose up -d
```
**Servicios incluidos**:

- `PostgreSQL + TimescaleDB`
- `Redis`
- `RabbitMQ`

---


## 📡 Ejecutar Aplicacion
```
mvn clean install
mvn spring-boot:run
```
Disponible en:
```
http://localhost:8080
```

## 📡 Endpoints Principales

### Vehículos
- `POST /vehicles`
- `GET /vehicles/{id}`

### Señales
- `POST /signals`

### Emergencias
- `POST /emergency`

### Health
- `GET /health` 

---

## 🧪 Testing

### API Testing
```bash
mvn test
```
### Incluye pruebas de:

- `RuleEngine`
- `ProcessSignalService`


## 📈 Estrategia de Escalabilidad
#### Separación OLTP / OLAP

- Evita lock contention y degradación de consultas

#### RabbitMQ con Colas Priorizadas

- Emergency Queue (SLA < 2s)

- Signals Queue

- Notifications Queue

#### Dual DataSource

- Pool OLTP: 20 conexiones

- Pool Telemetría: 50 conexiones

#### Cache Redis Multi-Nivel

- L1: Memoria local
- L2: Redis
- Escala desde 500 señales/s hasta 5,000+ señales/s agregando workers

---
## 🛡️ Estrategia de Disponibilidad

- Servicios stateless

- Workers independientes

- DLQ habilitada

- Multi-AZ en producción

- Sin punto único de falla

- Emergencias garantizadas < 2 segundos

---
## Recomendación para Migracion a entorno de Producción en Nube AWS

- Kubernetes (EKS)

- RDS Multi-AZ

- Redis Cluster

- RabbitMQ Cluster

- Autoscaling horizontal

---

## 👨‍💻 Author

**Edwin Yhovany Garzon**
- GitHub: [@edwinyhovany]
---

## 📧 Contact

For questions or collaboration: edwin-ega@hotmail.com

---
