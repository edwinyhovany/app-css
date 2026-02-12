package com.ccs.monitoring.infrastructure.adapter.in.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    
    public static final String EMERGENCY_QUEUE = "ccs.emergency.queue";
    public static final String SIGNALS_QUEUE = "ccs.signals.queue";
    public static final String NOTIFICATIONS_QUEUE = "ccs.notifications.queue";
    public static final String EXCHANGE = "ccs.exchange";
    
    // Cola de EMERGENCIAS - Máxima prioridad
    @Bean
    public Queue emergencyQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        args.put("x-message-ttl", 60000); // 1 minuto TTL
        return new Queue(EMERGENCY_QUEUE, true, false, false, args);
    }
    
    // Cola de SEÑALES normales
    @Bean
    public Queue signalsQueue() {
        return new Queue(SIGNALS_QUEUE, true);
    }
    
    // Cola de NOTIFICACIONES
    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE, true);
    }
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
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
    
    // Factory para EMERGENCIAS - Más threads
    @Bean
    public SimpleRabbitListenerContainerFactory emergencyListenerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = 
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(1);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
    
    // Factory para SEÑALES normales
    @Bean
    public SimpleRabbitListenerContainerFactory signalsListenerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = 
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(8);
        factory.setPrefetchCount(10);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
