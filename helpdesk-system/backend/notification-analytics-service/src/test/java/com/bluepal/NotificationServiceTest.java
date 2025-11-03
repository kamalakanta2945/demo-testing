package com.bluepal;

import com.bluepal.dto.TicketResponse;
import com.bluepal.dto.UserResponse;
import com.bluepal.email.EmailService;
import com.bluepal.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    public static MockWebServer mockBackEnd;

    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmailService emailService;

    @Mock
    private WebClient.Builder webClientBuilder;

    private final CountDownLatch latch = new CountDownLatch(1);

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        notificationService = new NotificationService(emailService, webClientBuilder);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(emailService).sendEmail(any(), any(), any());
    }

    @Test
    void testSendTicketCreationNotification() throws JsonProcessingException, InterruptedException {
        UserResponse user = new UserResponse(1L, "testuser", "test@example.com", "ROLE_USER");
        mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(user))
                .addHeader("Content-Type", "application/json"));

        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(user));


        TicketResponse ticket = new TicketResponse();
        ticket.setId(1L);
        ticket.setTitle("Test Ticket");
        ticket.setCreatedBy("testuser");

        notificationService.sendTicketCreationNotification(ticket);

        latch.await(5, TimeUnit.SECONDS);

        verify(emailService).sendEmail(any(), any(), any());
    }
}
