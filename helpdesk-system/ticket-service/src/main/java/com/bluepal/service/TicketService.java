package com.bluepal.service;

import com.bluepal.dto.ReplyRequest;
import com.bluepal.dto.TicketCreateRequest;
import com.bluepal.dto.TicketResponse;
import com.bluepal.entity.Reply;
import com.bluepal.entity.Ticket;
import com.bluepal.producer.TicketEventProducer;
import com.bluepal.repository.ReplyRepository;
import com.bluepal.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ReplyRepository replyRepository;
    private final TicketEventProducer ticketEventProducer;

    public TicketService(TicketRepository ticketRepository, ReplyRepository replyRepository, TicketEventProducer ticketEventProducer) {
        this.ticketRepository = ticketRepository;
        this.replyRepository = replyRepository;
        this.ticketEventProducer = ticketEventProducer;
    }

    public TicketResponse createTicket(TicketCreateRequest request, String username) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setDepartment(request.getDepartment());
        ticket.setCreatedBy(username);
        Ticket savedTicket = ticketRepository.save(ticket);
        TicketResponse response = new TicketResponse(savedTicket);
        ticketEventProducer.sendTicketCreatedEvent(response);
        return response;
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream().map(TicketResponse::new).collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return new TicketResponse(ticket);
    }

    public void addReply(Long ticketId, ReplyRequest request, String username) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        Reply reply = new Reply();
        reply.setTicketId(ticketId);
        reply.setContent(request.getContent());
        reply.setCreatedBy(username);
        replyRepository.save(reply);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    public void assignTicket(Long ticketId, String agent) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setAssignedTo(agent);
        ticket.setStatus("IN_PROGRESS");
        ticketRepository.save(ticket);
    }
}
