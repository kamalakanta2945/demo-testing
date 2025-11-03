package com.bluepal.service;

import com.bluepal.dto.TicketCreateRequest;
import com.bluepal.entity.Ticket;
import com.bluepal.producer.TicketEventProducer;
import com.bluepal.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketEventProducer ticketEventProducer;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testCreateTicket() {
        TicketCreateRequest request = new TicketCreateRequest();
        request.setTitle("Test Ticket");
        request.setDescription("Test Description");
        request.setDepartment("Test Department");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        ticketService.createTicket(request, "testuser");

        verify(ticketRepository).save(any(Ticket.class));
        verify(ticketEventProducer).sendTicketCreatedEvent(any());
    }
}
