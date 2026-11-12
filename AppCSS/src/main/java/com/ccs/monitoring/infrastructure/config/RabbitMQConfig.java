package com.ccs.monitoring.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración de RabbitMQ con serialización JSON
 * 
 * DECISIÓN ARQUITECTÓNICA:
 * - Se usa JSON en lugar de Java Serialization por:
 *   1. Interoperabilidad: Otros lenguajes pueden consumir mensajes
 *   2. Seguridad: Sin riesgos de deserialización arbitraria
 *   3. Portabilidad: No depende de versiones de clases Java
 *   4. Observabilidad: Mensajes legibles en RabbitMQ Management UI
 *   5. Estándar: JSON es el estándar de facto en sistemas distribuidos
 */
@Configuration
public class RabbitMQConfig {
    
    public static final String EMERGENCY_QUEUE = "ccs.emergency.queue";
    public static final String SIGNALS_QUEUE = "ccs.signals.queue";
    public static final String NOTIFICATIONS_QUEUE = "ccs.notifications.queue";
    public static final String EXCHANGE = "ccs.exchange";
    
   /* // ==========================================
    // OBJECT MAPPER
    // ==========================================
    
    @Bean
    public ObjectMapper rabbitObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    
    // ==========================================
    // CLASS MAPPER - Type Mapping
    // ==========================================
    
    /**
     * ClassMapper que mapea nombres simples a clases Java
     * 
     * Esto permite:
     * - Enviar "__TypeId__: signal" en el header
     * - Deserializar automáticamente como SignalDTO
     * - No depender del fully qualified class name
     */
   /* @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        
        // Mapeo: nombre simple → clase Java
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("signal", SignalDTO.class);
        // Agregar más DTOs si es necesario
        // idClassMapping.put("vehicle", VehicleDTO.class);
        // idClassMapping.put("rule", RuleDTO.class);
        
        classMapper.setIdClassMapping(idClassMapping);
        
        return classMapper;
    }*/
    // ==========================================
    // MESSAGE CONVERTER - JSON
    // ==========================================
    
    /**
     * Converter para serializar/deserializar mensajes como JSON
     * 
     * Esto permite:
     * - DTOs se convierten automáticamente a JSON
     * - No requiere implements Serializable
     * - Consumers en otros lenguajes pueden leer los mensajes
     * - Mensajes son lejibles y entendibles en RabbitMQ UI
     */
  
 
   @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
       /**
     * RabbitTemplate configurado con JSON converter
     * 
     * CRÍTICO: Este bean configura el template que usa el Controller
     * para enviar mensajes. Sin esto, usaría SimpleMessageConverter
     * que requiere Serializable.
     */
    
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setMandatory(true);

        template.setReturnsCallback(returned -> {
            System.err.println("Message returned: " +
                new String(returned.getMessage().getBody()));
        });

        return template;
    }
    
    // ==========================================
    // QUEUES - Con prioridades
    // ==========================================
    
    /**
     * Cola de EMERGENCIAS
     * - Prioridad máxima (x-max-priority: 10)
     * - TTL corto (60s) para no retener emergencias antiguas
     */
    @Bean
    public Queue emergencyQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        args.put("x-message-ttl", 60000); // 60 segundos
        return new Queue(EMERGENCY_QUEUE, true, false, false, args);
    }
    
    /**
     * Cola de señales normales
     * - Sin prioridad especial
     * - Durable (sobrevive a restart de RabbitMQ)
     */
    @Bean
    public Queue signalsQueue() {
        return new Queue(SIGNALS_QUEUE, true);
    }
    
    /**
     * Cola de notificaciones
     * - Para acciones asíncronas (SMS, Email, etc.)
     */
    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE, true);
    }
    
    // ==========================================
    // EXCHANGE & BINDINGS
    // ==========================================
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }
    
    @Bean
    public Binding emergencyBinding() {
        return BindingBuilder
            .bind(emergencyQueue())
            .to(exchange())
            .with("signal.emergency");
    }
    
    @Bean
    public Binding signalsBinding() {
        return BindingBuilder
            .bind(signalsQueue())
            .to(exchange())
            .with("signal.normal");
    }
    
    @Bean
    public Binding notificationsBinding() {
        return BindingBuilder
            .bind(notificationsQueue())
            .to(exchange())
            .with("notification.#");
    }
    
    // ==========================================
    // LISTENER FACTORIES - Consumers
    // ==========================================
    
    /**
     * Factory para EMERGENCIAS
     * - 5 consumers concurrentes (procesamiento rápido)
     * - Prefetch: 1 (cada worker procesa 1 mensaje a la vez)
     * - JSON converter para deserializar mensajes
     */

    @Bean
public SimpleRabbitListenerContainerFactory emergencyListenerFactory(
        ConnectionFactory connectionFactory,
        MessageConverter messageConverter) {

    SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();

    factory.setConnectionFactory(connectionFactory);
    factory.setConcurrentConsumers(5);
    factory.setMaxConcurrentConsumers(10);
    factory.setPrefetchCount(1);

    factory.setMessageConverter(messageConverter); //  Json correcto

    return factory;
}
    /**
     * Factory para señales NORMALES
     * - 3-8 consumers concurrentes (throughput alto)
     * - Prefetch: 10 (batch processing)
     * - JSON converter
     */
    @Bean
    public SimpleRabbitListenerContainerFactory signalsListenerFactory(
            ConnectionFactory connectionFactory,  
            MessageConverter messageConverter) {
  
    SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(8);
        factory.setPrefetchCount(10);
        factory.setMessageConverter(messageConverter); //  Json correcto
        return factory;
    }
}