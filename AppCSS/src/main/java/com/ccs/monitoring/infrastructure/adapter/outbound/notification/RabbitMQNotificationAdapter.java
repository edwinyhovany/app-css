package com.ccs.monitoring.infrastructure.adapter.outbound.notification;

import com.ccs.monitoring.domain.model.Alert;
import com.ccs.monitoring.domain.port.out.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQNotificationAdapter implements NotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(RabbitMQNotificationAdapter.class);
    private static final String EXCHANGE = "ccs.exchange";
    
    private final RabbitTemplate rabbitTemplate;
    
    public RabbitMQNotificationAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public void sendSMS(String phoneNumber, String message) {
        log.info("📱 Sending SMS to {}: {}", phoneNumber, message);
        
        // En producción, integrar con servicio SMS (Twilio, AWS SNS, etc.)
        // Por ahora, simular envío
        try {
            rabbitTemplate.convertAndSend(
                EXCHANGE,
                "notification.sms",
                new NotificationMessage("SMS", phoneNumber, message)
            );
        } catch (Exception e) {
            log.error("Error sending SMS notification", e);
        }
    }
    
    @Override
    public void sendEmail(String email, String subject, String body) {
        log.info("📧 Sending email to {}: {}", email, subject);
        
        // En producción, integrar con servicio de email (SendGrid, AWS SES, etc.)
        try {
            rabbitTemplate.convertAndSend(
                EXCHANGE,
                "notification.email",
                new NotificationMessage("EMAIL", email, subject + " - " + body)
            );
        } catch (Exception e) {
            log.error("Error sending email notification", e);
        }
    }
    
    @Override
    public void callAuthorities(Alert alert) {
        log.warn("🚨 Calling authorities for CRITICAL alert: {}", alert.getMessage());
        
        // En producción, integrar con sistema de emergencias
        try {
            rabbitTemplate.convertAndSend(
                EXCHANGE,
                "notification.authorities",
                new NotificationMessage("AUTHORITIES", "emergency@authorities.gov", alert.getMessage())
            );
        } catch (Exception e) {
            log.error("Error calling authorities", e);
        }
    }
    
    @Override
    public void sendPushNotification(String userId, String message) {
        log.info("🔔 Sending push notification to user {}: {}", userId, message);
        
        // En producción, integrar con servicio push (Firebase, OneSignal, etc.)
        try {
            rabbitTemplate.convertAndSend(
                EXCHANGE,
                "notification.push",
                new NotificationMessage("PUSH", userId, message)
            );
        } catch (Exception e) {
            log.error("Error sending push notification", e);
        }
    }
    
    // DTO interno
    private static class NotificationMessage {
        private String type;
        private String recipient;
        private String content;
        
        public NotificationMessage(String type, String recipient, String content) {
            this.type = type;
            this.recipient = recipient;
            this.content = content;
        }
        
        public String getType() { return type; }
        public String getRecipient() { return recipient; }
        public String getContent() { return content; }
    }
}
