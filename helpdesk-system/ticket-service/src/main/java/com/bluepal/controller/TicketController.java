package com.bluepal.controller;

import com.bluepal.dto.ReplyRequest;
import com.bluepal.dto.TicketCreateRequest;
import com.bluepal.dto.TicketResponse;
import com.bluepal.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketCreateRequest request, Principal principal) {
        return ResponseEntity.ok(ticketService.createTicket(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping("/{id}/replies")
    public ResponseEntity<?> addReply(@PathVariable Long id, @RequestBody ReplyRequest request, Principal principal) {
        ticketService.addReply(id, request, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/assign/{agent}")
    public ResponseEntity<?> assignTicket(@PathVariable Long id, @PathVariable String agent) {
        ticketService.assignTicket(id, agent);
        return ResponseEntity.ok().build();
    }
}
