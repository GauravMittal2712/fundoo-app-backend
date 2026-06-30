package com.bridgelabz.fundoo.listener;

import com.bridgelabz.fundoo.entity.PasswordResetToken;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.messaging.UserEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    private final UserEventProducer eventProducer;

    @EventListener
    @Async
    public void handleUserRegistrationEvent(User user) {
        log.info("Received Spring Event for User Registration: {}", user.getEmail());
        
        String message = String.format("{\"userId\": %d, \"email\": \"%s\", \"firstName\": \"%s\", \"action\": \"REGISTERED\"}",
                user.getId(), user.getEmail(), user.getFirstName());
        
        // Publish to Kafka user-events topic
        eventProducer.sendEvent("user-events", String.valueOf(user.getId()), message);

        // Also publish to audit log topic
        String auditMessage = String.format("{\"eventType\": \"USER_REGISTRATION\", \"details\": \"User %s registered successfully\"}", user.getEmail());
        eventProducer.sendEvent("audit-logs", String.valueOf(user.getId()), auditMessage);
    }

    @EventListener
    @Async
    public void handleForgotPasswordEvent(PasswordResetToken token) {
        log.info("Received Spring Event for Forgot Password: {}", token.getUser().getEmail());

        String message = String.format("{\"email\": \"%s\", \"token\": \"%s\", \"action\": \"PASSWORD_RESET_REQUESTED\"}",
                token.getUser().getEmail(), token.getToken());

        // Publish to Kafka user-events topic
        eventProducer.sendEvent("user-events", token.getUser().getEmail(), message);
    }
}
