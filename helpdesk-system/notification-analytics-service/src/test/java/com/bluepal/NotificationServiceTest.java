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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationServiceTest {

    public static MockWebServer mockBackEnd;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

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
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(baseUrl);
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

        TicketResponse ticket = new TicketResponse();
        ticket.setId(1L);
        ticket.setTitle("Test Ticket");
        ticket.setCreatedBy("testuser");

        notificationService.sendTicketCreationNotification(ticket);

        latch.await(5, TimeUnit.SECONDS);

        verify(emailService).sendEmail(any(), any(), any());
    }
}
