package com.bluepal.service;

import com.bluepal.dto.TicketResponse;
import com.bluepal.dto.UserResponse;
import com.bluepal.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(EmailService emailService, WebClient.Builder webClientBuilder) {
        this.emailService = emailService;
        this.webClientBuilder = webClientBuilder;
    }

    public void sendTicketCreationNotification(TicketResponse ticket) {
        webClientBuilder.build().get()
                .uri("lb://auth-user-service/api/users/" + ticket.getCreatedBy())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnError(e -> logger.error("Error fetching user email: {}", e.getMessage()))
                .subscribe(user -> {
                    String to = user.getEmail();
                    String subject = "New Ticket Created: " + ticket.getTitle();
                    String body = "A new ticket has been created with ID: " + ticket.getId();
                    emailService.sendEmail(to, subject, body);
                });
    }
}
