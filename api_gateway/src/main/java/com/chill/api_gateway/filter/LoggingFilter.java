package com.chill.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;


@Component
@Slf4j
public class LoggingFilter implements GlobalFilter {
    @Value("${USER_URI:localhost:8080}") String userBaseUri;

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getRawPath();
        logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), path);

        // this attribute is a URI, not a List<URI>
        URI targetUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        if (targetUri != null) {
            logger.info(" └─> proxying to: {}", targetUri);
        }

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    logger.info("Response status for {}: {}",
                            path, exchange.getResponse().getStatusCode());
                })
        );
    }
}
