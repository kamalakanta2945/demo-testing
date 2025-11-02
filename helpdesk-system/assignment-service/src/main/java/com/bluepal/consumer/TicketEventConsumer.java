package com.bluepal.consumer;

import com.bluepal.dto.TicketResponse;
import com.bluepal.service.AssignmentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketEventConsumer {

    private final AssignmentService assignmentService;

    public TicketEventConsumer(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(TicketResponse ticket) {
        if ("ticket-created".equals(ticket.getStatus())) {
            assignmentService.assignTicket(ticket);
        }
    }
}
