package com.bluepal;

import com.bluepal.dto.TicketResponse;
import com.bluepal.dto.UserResponse;
import com.bluepal.repository.AgentLoadRepository;
import com.bluepal.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    private AssignmentService assignmentService;

    @Mock
    private AgentLoadRepository agentLoadRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void initialize() {
        assignmentService = new AssignmentService(agentLoadRepository, webClientBuilder);
    }

    @Test
    void testAssignTicket() {
        List<UserResponse> admins = List.of(new UserResponse(1L, "admin1", "admin1@test.com", "ROLE_ADMIN"));
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);

        // Mock the call to auth-user-service
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://auth-user-service/api/users/admins")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.fromIterable(admins));

        // Mock the call to ticket-service
        WebClient.RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Mockito.anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        TicketResponse ticket = new TicketResponse();
        ticket.setId(1L);

        assignmentService.assignTicket(ticket);

        verify(agentLoadRepository).save(any());
    }
}
