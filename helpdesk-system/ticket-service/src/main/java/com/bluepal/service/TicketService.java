package com.bluepal.service;

import com.bluepal.dto.ReplyRequest;
import com.bluepal.dto.ReplyResponse;
import com.bluepal.dto.TicketCreateRequest;
import com.bluepal.dto.TicketResponse;
import com.bluepal.entity.Reply;
import com.bluepal.entity.Ticket;
import com.bluepal.producer.TicketEventProducer;
import com.bluepal.repository.ReplyRepository;
import com.bluepal.repository.TicketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        TicketResponse response = toTicketResponse(savedTicket);
        ticketEventProducer.sendTicketCreatedEvent(response);
        return response;
    }

    public List<TicketResponse> getAllTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        if (isAdmin) {
            return ticketRepository.findAll().stream().map(this::toTicketResponse).collect(Collectors.toList());
        } else {
            return ticketRepository.findByCreatedBy(username).stream().map(this::toTicketResponse).collect(Collectors.toList());
        }
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return toTicketResponse(ticket);
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

    public List<ReplyResponse> getReplies(Long ticketId) {
        return replyRepository.findByTicketId(ticketId).stream().map(this::toReplyResponse).collect(Collectors.toList());
    }

    private TicketResponse toTicketResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setStatus(ticket.getStatus());
        response.setDepartment(ticket.getDepartment());
        response.setCreatedBy(ticket.getCreatedBy());
        response.setAssignedTo(ticket.getAssignedTo());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());
        return response;
    }

    private ReplyResponse toReplyResponse(Reply reply) {
        return new ReplyResponse(reply.getId(), reply.getContent(), reply.getCreatedBy(), reply.getCreatedAt());
    }
}
