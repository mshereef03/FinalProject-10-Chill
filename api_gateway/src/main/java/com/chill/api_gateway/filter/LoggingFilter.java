import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getRawPath();

        log.info("Incoming request: {} {}", exchange.getRequest().getMethod(), path);

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    int status = exchange.getResponse().getStatusCode().value();
                    log.info("Response status for {}: {}", path, status);
                })
        );
    }
}
