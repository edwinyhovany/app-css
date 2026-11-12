# **Diagrama de Despliegue (Vista Fisica)**

PlantUML Web Server \- Generador Diagramas

[https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000](https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000)

@startuml  
title Diagrama de Despliegue Físico \- CCS Monitoring (On-Premise)

skinparam node {  
    BackgroundColor\<\<Server\>\> \#FFFFFF  
    BackgroundColor\<\<Subnet\>\> \#FFFFFF  
}

skinparam device {  
    BackgroundColor \#F4F6F7  
}

' Servidor Físico  
node "Servidor On-Premise (Proxmox/ESXi/Bare Metal)" \<\<Server\>\> {

    ' Red Perimetral  
    node "DMZ (Public Subnet: 10.0.1.x)" \<\<Subnet\>\> {  
        node "Container: Security Layer" \<\<Container\>\> as SecCont {  
             \[WAF / Firewall\] as WAF  
             \[NGINX (Reverse Proxy)\] as Nginx  
        }  
    }

    ' Red de Aplicación  
    node "Private Subnet Application (10.0.2.x)" \<\<Subnet\>\> {  
        node "Container: Authentication-Authorization" \<\<Container\>\> as AuthCont {  
            \[Spring Cloud Gateway\] as Gateway  
        }

        node "Container: Embedded Server Application" \<\<Container\>\> as ApiCont {  
            \[CCS Monitoring API\\n(Flow Application \- Java 21)\] as Api  
        }  
    }

    ' Red de Datos  
    node "Private Subnet Data (10.0.3.x)" \<\<Subnet\>\> {  
        node "Container: Redis Cluster" \<\<Container\>\> {  
            \[Redis (ElastiCache Style)\] as Redis  
        }  
        node "Container: RabbitMQ Cluster" \<\<Container\>\> {  
            \[RabbitMQ\] as MQ  
        }  
        node "Container: Database Server" \<\<Container\>\> {  
            database "RDS PostgreSQL\\n+ TimescaleDB" as DB  
        }  
    }  
}

' Relaciones de Despliegue y Comunicación  
WAF \-down-\> Nginx : HTTP/HTTPS  
Nginx \-down-\> Gateway : Proxy Pass (Port 8080\)  
Gateway \-down-\> Api : Forward (Internal Port)

Api \-down-\> Redis : Cache Protocol  
Api \-down-\> MQ : AMQP  
Api \-down-\> DB : JDBC/SQL

@enduml

# **Diagrama de Contexto (C4 Nivel 1\)**

PlantUML Web Server \- Generador Diagramas

[https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000](https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000)

@startuml C4\_Context  
\!include [https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4\_Context.puml](https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml)

title CCS Monitoring \- System Context Diagram (C4 Level 1\)

Person(driver, "Driver / Vehicle Device", "Dispositivo GPS o IoT que envía señales")  
Person(operator, "Operations Operator", "Configura reglas y monitorea alertas")

System(system, "CCS Monitoring System", "Sistema de monitoreo de vehículos en tiempo real")

System\_Ext(sms, "SMS Gateway", "Proveedor de SMS")  
System\_Ext(email, "Email Service", "Servicio de correo")  
System\_Ext(authorities, "Emergency Authorities", "Servicios de emergencia")

Rel(driver, system, "Envía señales", "REST/JSON")  
Rel(operator, system, "Configura reglas y vehículos", "REST/JSON")

Rel(system, sms, "Envía SMS", "API")  
Rel(system, email, "Envía Emails", "SMTP/API")  
Rel(system, authorities, "Notifica emergencias", "API")

@enduml

# **Diagrama de Contenedores (C4 Nivel 2\)**

PlantUML Web Server \- Generador Diagramas  
[https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000](https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000)

@startuml  
\!include [https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4\_Container.puml](https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml)

title CCS Monitoring \- Container Diagram (C4 Level 2\)

Person(user, "Operator", "Administra vehículos y reglas")

System(system, "CCS Monitoring System")

System\_Boundary(systemBoundary, "CCS Monitoring") {

Container(api,"Monitoring API","Spring Boot","Procesa señales y evalúa reglas")

ContainerDb(postgres,"PostgreSQL \+ TimescaleDB","Database","Datos operacionales y telemetría")

Container(redis,"Redis","Cache","Cache de reglas activas")

Container(rabbit,"RabbitMQ","Message Broker","Eventos y notificaciones")

}

Rel(user, api, "REST / HTTPS")

Rel(api, postgres, "JPA / JDBC")  
Rel(api, redis, "Cache-aside")  
Rel(api, rabbit, "AMQP")  
@enduml

# **Diagrama de Componentes (C4 Nivel 3\)**

PlantUML Web Server \- Generador Diagramas

[https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000](https://www.plantuml.com/plantuml/uml/ut8eBaaiAYdDpU5opCbCJbNGjLDmoa-oKd0iBSb8pIl9J4uioSpFKmXABInDBIxX0iefw0BLW1LZKLLSa9zNdCg5RbXUFb1TaK8YTaCXYcrqTGceXXhka9gN0d8m0000)

## **Formato 1 (Version Lite) \- startml **

@startuml C4\_Component  
\!include [https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4\_Component.puml](https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml)

title CCS Monitoring \- Component Diagram (C4 Level 3\)

Container(api, "CCS Monitoring API", "Spring Boot", "Hexagonal Architecture")

Component(ctrl\_signal, "SignalController", "REST Controller", "Recibe señales")  
Component(ctrl\_rule, "RuleController", "REST Controller", "Configura reglas")  
Component(ctrl\_vehicle, "VehicleController", "REST Controller", "Admin vehículos")

Component(app\_process, "ProcessSignalService", "Application Service", "Orquesta procesamiento")  
Component(app\_emergency, "HandleEmergencyService", "Application Service", "Gestión emergencia \<2s")  
Component(app\_config\_rule, "ConfigureRuleService", "Application Service", "Configura reglas")

Component(domain\_rule\_engine, "RuleEngine", "Domain Service", "Evalúa reglas")

Component(repo\_vehicle, "VehicleRepository", "Port Out")  
Component(repo\_rule, "RuleRepository", "Port Out")  
Component(repo\_signal, "SignalRepository", "Port Out")  
Component(repo\_alert, "AlertRepository", "Port Out")

Component(redis\_adapter, "RedisCacheAdapter", "Cache Adapter")  
Component(rabbit\_adapter, "RabbitMQNotificationAdapter", "Messaging Adapter")

ContainerDb(postgres, "PostgreSQL \+ TimescaleDB", "RDS", "OLTP \+ Time-series")  
Container(redis, "Redis", "Cache reglas activas")  
Container(rabbit, "RabbitMQ", "Event Bus")

Rel(ctrl\_signal, app\_process, "Invoca")  
Rel(ctrl\_rule, app\_config\_rule, "Invoca")  
Rel(ctrl\_vehicle, repo\_vehicle, "Administra")

Rel(app\_process, domain\_rule\_engine, "Evalúa")  
Rel(app\_process, repo\_signal, "Persiste señal")  
Rel(app\_process, redis\_adapter, "Obtiene reglas")  
Rel(app\_process, rabbit\_adapter, "Publica evento")

Rel(app\_emergency, repo\_signal, "Persiste emergencia")  
Rel(app\_emergency, rabbit\_adapter, "Publica EmergencyEvent")

Rel(repo\_vehicle, postgres, "JPA")  
Rel(repo\_rule, postgres, "JPA")  
Rel(repo\_signal, postgres, "JPA")  
Rel(repo\_alert, postgres, "JPA")

Rel(redis\_adapter, redis, "Cache-aside")  
Rel(rabbit\_adapter, rabbit, "AMQP")

@enduml

## **Formato 2 (Versión Profesional)  \- startml **

@startuml  
\!include [https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4\_Component.puml](https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml)

title CCS Monitoring \- Component Diagram (C4 Level 3 Hexagonal Architecture)

Container(api,"CCS Monitoring API","Spring Boot","Hexagonal Architecture")

Component(ctrl\_signal,"SignalController","REST Controller","Recibe señales")  
Component(ctrl\_rule,"RuleController","REST Controller","Configura reglas")  
Component(ctrl\_vehicle,"VehicleController","REST Controller","Administra vehículos")

Component(app\_process,"ProcessSignalService","Application Service","Procesa señales")  
Component(app\_emergency,"HandleEmergencyService","Application Service","Procesa emergencias \<2s")  
Component(app\_config,"ConfigureRuleService","Application Service","Gestiona reglas")

Component(domain\_engine,"RuleEngine","Domain Service","Evalúa reglas")

Component(port\_signal,"SignalRepository","Port Out","Persistencia señales")  
Component(port\_rule,"RuleRepository","Port Out","Persistencia reglas")  
Component(port\_vehicle,"VehicleRepository","Port Out","Persistencia vehículos")  
Component(port\_alert,"AlertRepository","Port Out","Persistencia alertas")  
Component(port\_notify,"NotificationService","Port Out","Envío notificaciones")  
Component(port\_cache,"CacheService","Port Out","Cache reglas")

Component(adapter\_jpa\_signal,"SignalJpaAdapter","JPA Adapter","Implementa SignalRepository")  
Component(adapter\_jpa\_rule,"RuleJpaAdapter","JPA Adapter","Implementa RuleRepository")  
Component(adapter\_jpa\_vehicle,"VehicleJpaAdapter","JPA Adapter","Implementa VehicleRepository")  
Component(adapter\_jpa\_alert,"AlertJpaAdapter","JPA Adapter","Implementa AlertRepository")

Component(adapter\_redis,"RedisCacheAdapter","Cache Adapter","Implementa CacheService")  
Component(adapter\_rabbit,"RabbitMQNotificationAdapter","Messaging Adapter","Implementa NotificationService")

ContainerDb(postgres,"PostgreSQL \+ TimescaleDB","RDS","OLTP \+ Time-series")  
Container(redis,"Redis","Cache reglas activas")  
Container(rabbit,"RabbitMQ","Event Bus")

Rel(ctrl\_signal,app\_process,"Invoca")  
Rel(ctrl\_signal,app\_emergency,"Invoca emergencia")  
Rel(ctrl\_rule,app\_config,"Invoca")  
Rel(ctrl\_vehicle,port\_vehicle,"Administra")

Rel(app\_process,domain\_engine,"Evalúa reglas")  
Rel(app\_process,port\_signal,"Persiste señal")  
Rel(app\_process,port\_rule,"Obtiene reglas")  
Rel(app\_process,port\_alert,"Genera alertas")  
Rel(app\_process,port\_notify,"Notifica")  
Rel(app\_process,port\_cache,"Cache reglas")  
Rel(app\_config,port\_rule,"Obtiene reglas")

Rel(app\_emergency,port\_signal,"Persiste emergencia")  
Rel(app\_emergency,port\_alert,"Genera alerta")  
Rel(app\_emergency,port\_notify,"Notifica")

Rel(port\_signal,adapter\_jpa\_signal,"Implementación")  
Rel(port\_rule,adapter\_jpa\_rule,"Implementación")  
Rel(port\_vehicle,adapter\_jpa\_vehicle,"Implementación")  
Rel(port\_alert,adapter\_jpa\_alert,"Implementación")  
Rel(port\_cache,adapter\_redis,"Implementación")  
Rel(port\_notify,adapter\_rabbit,"Implementación")

Rel(adapter\_jpa\_signal,postgres,"JPA")  
Rel(adapter\_jpa\_rule,postgres,"JPA")  
Rel(adapter\_jpa\_vehicle,postgres,"JPA")  
Rel(adapter\_jpa\_alert,postgres,"JPA")

Rel(adapter\_redis,redis,"Cache-aside")  
Rel(adapter\_rabbit,rabbit,"AMQP")  
@enduml

## **Formato 3 (compatible) \- startml **

@startuml  
title CCS Monitoring \- Component Diagram

package "CCS Monitoring API (Hexagonal Architecture)" {

  component "SignalController"  
  component "RuleController"  
  component "VehicleController"

  component "ProcessSignalService"  
  component "HandleEmergencyService"  
  component "ConfigureRuleService"

  component "RuleEngine (Domain)"

  component "VehicleRepository"  
  component "RuleRepository"  
  component "SignalRepository"  
  component "AlertRepository"

  component "RedisCacheAdapter"  
  component "RabbitMQNotificationAdapter"  
}

database "PostgreSQL \+ TimescaleDB" as DB  
component "Redis" as REDIS  
component "RabbitMQ" as MQ

"SignalController" \--\> "ProcessSignalService"  
"RuleController" \--\> "ConfigureRuleService"

"ProcessSignalService" \--\> "RuleEngine (Domain)"  
"ProcessSignalService" \--\> "SignalRepository"  
"ProcessSignalService" \--\> "RedisCacheAdapter"  
"ProcessSignalService" \--\> "RabbitMQNotificationAdapter"

"HandleEmergencyService" \--\> "RabbitMQNotificationAdapter"

"VehicleRepository" \--\> DB  
"RuleRepository" \--\> DB  
"SignalRepository" \--\> DB  
"AlertRepository" \--\> DB

"RedisCacheAdapter" \--\> REDIS  
"RabbitMQNotificationAdapter" \--\> MQ

@enduml