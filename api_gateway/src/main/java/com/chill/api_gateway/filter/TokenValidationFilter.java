package com.chill.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenValidationFilter implements GlobalFilter, Ordered {

    @Value("${USER_URI:localhost:8080}") String userBaseUri;

    // WebClient can be injected/configured via builder if you wish.
    private final WebClient webClient = WebClient.create();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        String path = exchange.getRequest().getURI().getPath();
        if (!path.startsWith("/carts/checkout")) {
            return chain.filter(exchange);
        }

        // 1) Get “Authorization” header from incoming request
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If there is no Bearer header, reject immediately
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 2) Call user-service /users/verify-token
        return webClient.get()
                // Note: “user-service” is the Eureka‐registered service ID
                // and Gateway’s default port‐mapping (container→ container) is 8080.
                .uri("http://"+userBaseUri+"/users/verify-token")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(status -> status.value() == 401 || status.value() == 403, clientResp -> {
                    // If user-service returns 401/403, treat as invalid
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete()
                            .then(Mono.error(new RuntimeException("Token invalid")));
                })
                .bodyToMono(DecodedTokenDTO.class)        // <–– deserialize here
                .flatMap(tokenDto -> {
                    // 3) mutate the request to add headers
                    ServerHttpRequest mutatedReq = exchange.getRequest().mutate()
                            .header("X-Username", tokenDto.getUsername())
                            .header("X-Roles", String.join(",", tokenDto.getRoles()))
                            .build();

                    // 4) continue with the mutated exchange
                    return chain.filter(exchange.mutate().request(mutatedReq).build());
                });
    }

    @Override
    public int getOrder() {
        // Make sure this runs before the built-in routing filter.
        // A negative order ensures it’s very early in the filter chain.
        return -1;
    }
}