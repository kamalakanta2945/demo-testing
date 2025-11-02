package com.bluepal.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public JwtValidationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return chain.filter(exchange);
            }

            String token = authHeader.substring(7);

            return webClientBuilder.build()
                    .post()
                    .uri("http://auth-user-service/api/auth/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(ValidationResponse.class)
                    .flatMap(response -> {
                        exchange.getRequest().mutate().header("X-Username", response.getUsername());
                        return chain.filter(exchange);
                    })
                    .onErrorResume(e -> {
                        // Handle validation error
                        return Mono.error(new Exception("Invalid token"));
                    });
        };
    }

    public static class Config {
        // Put the configuration properties here
    }

    private static class ValidationResponse {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
