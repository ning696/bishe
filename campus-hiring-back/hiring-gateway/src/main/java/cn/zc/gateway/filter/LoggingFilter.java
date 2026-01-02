package cn.zc.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * 网关日志过滤器
 * 记录请求和响应信息，用于调试
 * 
 * @author campus-hiring-system
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();
        String query = request.getURI().getQuery();
        
        // 记录请求信息
        log.info("========== Gateway Request ==========");
        log.info("Method: {}", method);
        log.info("Path: {}", path);
        log.info("Query: {}", query != null ? query : "");
        log.info("Full URI: {}", request.getURI());
        log.info("Headers: {}", request.getHeaders().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ")));
        log.info("Remote Address: {}", request.getRemoteAddress());
        
        // 记录路由信息（在路由匹配后）
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {
            log.info("Route ID: {}", route.getId());
            log.info("Route URI: {}", route.getUri());
        }
        
        // 记录请求后的响应信息
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    log.info("========== Gateway Response ==========");
                    log.info("Status: {}", response.getStatusCode());
                    log.info("Response Headers: {}", response.getHeaders().entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining(", ")));
                    
                    // 检查是否有 Location 头（重定向）
                    String location = response.getHeaders().getFirst("Location");
                    if (location != null) {
                        log.info("Redirect Location: {}", location);
                    }
                }));
    }

    @Override
    public int getOrder() {
        return -50;  // 在路由匹配之后执行，可以记录路由信息
    }
}

