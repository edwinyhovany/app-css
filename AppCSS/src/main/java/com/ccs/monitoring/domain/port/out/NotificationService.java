package com.ccs.monitoring.domain.port.out;

import com.ccs.monitoring.domain.model.Alert;

public interface NotificationService {
    void sendSMS(String phoneNumber, String message);
    void sendEmail(String email, String subject, String body);
    void callAuthorities(Alert alert);
    void sendPushNotification(String userId, String message);
}
