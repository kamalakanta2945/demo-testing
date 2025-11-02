package com.bluepal.service;

import com.bluepal.dto.TicketResponse;
import com.bluepal.dto.UserResponse;
import com.bluepal.email.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationService {

    private final MailSender mailSender;
    private final WebClient.Builder webClientBuilder;

    public NotificationService(MailSender mailSender, WebClient.Builder webClientBuilder) {
        this.mailSender = mailSender;
        this.webClientBuilder = webClientBuilder;
    }

    public void sendTicketCreationNotification(TicketResponse ticket) {
        webClientBuilder.build().get()
                .uri("http://auth-user-service/api/users/" + ticket.getCreatedBy())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .subscribe(user -> {
                    String to = user.getEmail();
                    String subject = "New Ticket Created: " + ticket.getTitle();
                    String body = "A new ticket has been created with ID: " + ticket.getId();
                    mailSender.sendEmail(to, subject, body);
                });
    }
}
