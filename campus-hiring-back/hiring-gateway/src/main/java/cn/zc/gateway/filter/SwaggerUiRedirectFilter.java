package cn.zc.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Swagger UI 重定向过滤器
 * 处理 Swagger UI 通过网关访问时的路径重定向问题
 * 
 * @author campus-hiring-system
 */
@Slf4j
@Component
public class SwaggerUiRedirectFilter implements GlobalFilter, Ordered {

    // 匹配需要处理的前缀路径
    private static final Pattern SWAGGER_UI_PATTERN = Pattern.compile("^/(admin|student|enterprise)/swagger-ui.*");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 只处理 Swagger UI 相关的请求
        if (!SWAGGER_UI_PATTERN.matcher(path).matches()) {
            return chain.filter(exchange);
        }
        
        log.info("========== Swagger UI Redirect Filter ==========");
        log.info("Original Path: {}", path);
        
        // 提取服务前缀（admin、student 或 enterprise）
        String prefix = extractPrefix(path);
        log.info("Service Prefix: {}", prefix);
        
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        
        // 包装响应以修改内容和响应头
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = super.getHeaders();
                // 检查并修改 Location 响应头（每次访问 headers 时都会检查）
                modifyLocationHeader(headers, prefix);
                return headers;
            }
            
            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                // 在写入响应体之前，修改 Location 响应头
                modifyLocationHeader(getHeaders(), prefix);
                
                // 合并所有的 DataBuffer
                return DataBufferUtils.join(body)
                        .flatMap(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            
                            String bodyStr = new String(content, StandardCharsets.UTF_8);
                            log.info("Original Response Body Length: {}", bodyStr.length());
                            
                            // 只处理 HTML 响应
                            String contentType = getHeaders().getFirst("Content-Type");
                            if (contentType != null && contentType.contains("text/html")) {
                                // 修改响应体中的路径
                                String modifiedBody = modifySwaggerUiPaths(bodyStr, prefix);
                                
                                if (!bodyStr.equals(modifiedBody)) {
                                    log.info("Modified Swagger UI paths in response body");
                                }
                                
                                byte[] modifiedContent = modifiedBody.getBytes(StandardCharsets.UTF_8);
                                DataBuffer buffer = bufferFactory.wrap(modifiedContent);
                                getHeaders().setContentLength(modifiedContent.length);
                                
                                return super.writeWith(Mono.just(buffer));
                            } else {
                                // 非 HTML 响应，直接返回
                                return super.writeWith(Mono.just(bufferFactory.wrap(content)));
                            }
                        });
            }
        };
        
        // 执行过滤器链，并在响应返回后修改 Location 头
        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // 在响应返回后，再次检查并修改 Location 头（确保 302 重定向的 Location 被修改）
                    ServerHttpResponse response = exchange.getResponse();
                    modifyLocationHeader(response.getHeaders(), prefix);
                }));
    }
    
    /**
     * 修改 Location 响应头，添加服务前缀
     */
    private void modifyLocationHeader(HttpHeaders headers, String prefix) {
        String location = headers.getFirst("Location");
        if (location != null && (location.startsWith("/swagger-ui") || location.startsWith("/v3/api-docs"))) {
            // 如果 Location 缺少前缀，添加前缀
            if (!location.startsWith("/" + prefix + "/")) {
                String newLocation = "/" + prefix + location;
                headers.set("Location", newLocation);
                log.info("Modified Location header: {} -> {}", location, newLocation);
            }
        }
    }
    
    /**
     * 提取服务前缀
     */
    private String extractPrefix(String path) {
        if (path.startsWith("/admin/")) {
            return "admin";
        } else if (path.startsWith("/student/")) {
            return "student";
        } else if (path.startsWith("/enterprise/")) {
            return "enterprise";
        }
        return "";
    }
    
    /**
     * 修改 Swagger UI 响应体中的路径
     */
    private String modifySwaggerUiPaths(String body, String prefix) {
        if (prefix.isEmpty()) {
            return body;
        }
        
        String modified = body;
        
        // 替换 /swagger-ui/ 为 /{prefix}/swagger-ui/
        modified = modified.replaceAll("(/swagger-ui/)", "/" + prefix + "$1");
        modified = modified.replaceAll("(/swagger-ui\\.html)", "/" + prefix + "$1");
        
        // 替换 /v3/api-docs 为 /{prefix}/v3/api-docs
        modified = modified.replaceAll("(/v3/api-docs)", "/" + prefix + "$1");
        
        // 替换相对路径中的 swagger-ui
        modified = modified.replaceAll("(href|src|action)=\"([^\"]*swagger-ui[^\"]*)\"", 
                "$1=\"/" + prefix + "$2\"");
        
        // 替换 JavaScript 中的路径
        modified = modified.replaceAll("(['\"])(/swagger-ui/)", "$1/" + prefix + "$2");
        modified = modified.replaceAll("(['\"])(/v3/api-docs)", "$1/" + prefix + "$2");
        
        return modified;
    }

    @Override
    public int getOrder() {
        return -1;  // 在响应写入之前执行
    }
}

