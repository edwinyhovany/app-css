# 🚛 CCS PLatforma de Monitoreo

Plataforma central de monitoreo de vehículos para la Compañía Colombiana de Seguimiento (CCS), diseñada bajo Arquitectura Hexagonal, Domain-Driven Design y arquitectura orientada a eventos.

![Java](https://img.shields.io/badge/Java-17-orange)
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


### Architectural Style
- Hexagonal Architecture (Ports & Adapters)
- Domain-Driven Design
- Event-Driven Architecture
- CQRS (lightweight)
- Stateless Services

---

## 🚀 Tech Stack

### Backend
- **Language:** Java 17
- **Framework:** Spring Boot 3
- **Architecture:** Hexagonal + DDD
- **Persistence:** Spring Data JPA
- **Messaging:** RabbitMQ
- **Cache:** Redis (multi-level)
- **Database:** PostgreSQL + TimescaleDB
- **Migrations:** Flyway
- **Build Tool:** Maven
- **Containerization:** Docker

---

## 📊 Database Architecture

### OLTP Schema (`operational`)
- vehicles
- rules
- alerts

Optimized for:
- Transactions
- Real-time dashboards
- Fast updates

### OLAP Schema (`telemetry`)
- vehicle_signals (Hypertable)
- signals_hourly (Continuous aggregate)

Optimized for:
- High write throughput
- Time-series queries
- Automatic compression (after 7 days)
- 180-day retention policy

---

## 📦 Installation

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### Clone Repository
```bash
git clone https://github.com/YOUR_USERNAME/ccs-monitoring.git
cd hexagonal-users-api-mejorada

---
### ⚙️ Environment Variables

Create or configure application.yml or environment variables:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ccs
SPRING_DATASOURCE_USERNAME=ccs
SPRING_DATASOURCE_PASSWORD=ccs

REDIS_HOST=localhost
REDIS_PORT=6379

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

🐳 Run Infrastructure
docker-compose up -d


Services started:

PostgreSQL + TimescaleDB

Redis

RabbitMQ

🏃 Running the Application
mvn clean install
mvn spring-boot:run


API available at:

http://localhost:8080

📡 API Endpoints
Vehicles

POST /vehicles

GET /vehicles/{id}

Signals

POST /signals

Rules

POST /rules

GET /rules/{vehicleId}

Emergency

POST /emergency

Health

GET /health

🧪 Testing
mvn test


Includes:

RuleEngineTest

ProcessSignalServiceTest

SignalControllerTest

📈 Scalability Strategy
Database Isolation

Separate schemas for OLTP and OLAP

Avoids lock contention

RabbitMQ Prioritized Queues

Emergency queue (5 workers)

Signals queue (8 workers)

Notifications queue (2 workers)

Dual DataSource

OLTP Pool: 20 connections

Telemetry Pool: 50 connections

Redis Multi-Level Cache

L1: In-memory cache

L2: Redis

90% reduction in rule queries

System can scale from:

500 signals/sec

Up to 5,000 signals/sec by increasing workers

🛡️ Availability

Stateless services

Independent worker scaling

DLQ enabled

Multi-AZ ready

No single point of failure

Emergency SLA < 2 seconds

🚀 Deployment

Recommended Production Setup:

EKS / ECS Cluster

RDS PostgreSQL Multi-AZ

Redis Cluster

RabbitMQ Cluster

Horizontal Pod Autoscaler

👨‍💻 Author

CCS Monitoring Team

⭐ Contribution

Pull requests are welcome. Please open an issue first for discussion.
