# 🚛 CCS Monitoring Platform

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
