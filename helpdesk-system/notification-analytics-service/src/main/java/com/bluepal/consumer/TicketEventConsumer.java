package com.bluepal.consumer;

import com.bluepal.dto.TicketResponse;
import com.bluepal.service.AnalyticsService;
import com.bluepal.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketEventConsumer {

    private final NotificationService notificationService;
    private final AnalyticsService analyticsService;

    public TicketEventConsumer(NotificationService notificationService, AnalyticsService analyticsService) {
        this.notificationService = notificationService;
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(TicketResponse ticket) {
        if ("ticket-created".equals(ticket.getStatus())) {
            notificationService.sendTicketCreationNotification(ticket);
            analyticsService.incrementTicketCount();
        }
    }
}
