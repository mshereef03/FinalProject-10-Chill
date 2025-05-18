//package com.chill.api_gateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class TokenValidationFilter implements GlobalFilter, Ordered {
//
//    // WebClient can be injected/configured via builder if you wish.
//    private final WebClient webClient = WebClient.create();
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 1) Get “Authorization” header from incoming request
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            // If there is no Bearer header, reject immediately
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        // 2) Call user-service /users/verify-token
//        return webClient.get()
//                // Note: “user-service” is the Eureka‐registered service ID
//                // and Gateway’s default port‐mapping (container→ container) is 8080.
//                .uri("http://user-service:8080/users/verify-token")
//                .header(HttpHeaders.AUTHORIZATION, authHeader)
//                .retrieve()
//                .onStatus(status -> status.value() == 401 || status.value() == 403, clientResp -> {
//                    // If user-service returns 401/403, treat as invalid
//                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete()
//                            .then(Mono.error(new RuntimeException("Token invalid")));
//                })
//                .bodyToMono(Void.class)
//                // We don't actually need the body; if verify-token returns a DTO, you can use .bodyToMono(DecodedTokenDTO.class)
//                .flatMap(ignored -> {
//                    // 3) If user-service returned 200 (valid token), proceed with routing
//                    return chain.filter(exchange);
//                });
//    }
//
//    @Override
//    public int getOrder() {
//        // Make sure this runs before the built-in routing filter.
//        // A negative order ensures it’s very early in the filter chain.
//        return -1;
//    }
//}