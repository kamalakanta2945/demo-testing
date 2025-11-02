package com.bluepal.service;

import com.bluepal.dto.TicketResponse;
import com.bluepal.dto.UserResponse;
import com.bluepal.entity.AgentLoad;
import com.bluepal.repository.AgentLoadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AssignmentService {

    private final AgentLoadRepository agentLoadRepository;
    private final WebClient.Builder webClientBuilder;

    public AssignmentService(AgentLoadRepository agentLoadRepository, WebClient.Builder webClientBuilder) {
        this.agentLoadRepository = agentLoadRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public void assignTicket(TicketResponse ticket) {
        webClientBuilder.build().get()
                .uri("http://auth-user-service/api/users/admins")
                .retrieve()
                .bodyToFlux(UserResponse.class)
                .collectList()
                .flatMap(agents -> {
                    AgentLoad leastLoadedAgent = null;
                    int minTickets = Integer.MAX_VALUE;

                    for (UserResponse agent : agents) {
                        AgentLoad agentLoad = agentLoadRepository.findById(agent.getUsername()).orElse(new AgentLoad(agent.getUsername(), 0));
                        if (agentLoad.getAssignedTickets() < minTickets) {
                            minTickets = agentLoad.getAssignedTickets();
                            leastLoadedAgent = agentLoad;
                        }
                    }

                    if (leastLoadedAgent != null) {
                        ticket.setAssignedTo(leastLoadedAgent.getAgentUsername());
                        leastLoadedAgent.setAssignedTickets(leastLoadedAgent.getAssignedTickets() + 1);
                        agentLoadRepository.save(leastLoadedAgent);

                        return webClientBuilder.build().put()
                                .uri("http://ticket-service/api/tickets/" + ticket.getId() + "/assign/" + leastLoadedAgent.getAgentUsername())
                                .retrieve()
                                .bodyToMono(Void.class);
                    }
                    return Mono.empty();
                }).subscribe();
    }
}
