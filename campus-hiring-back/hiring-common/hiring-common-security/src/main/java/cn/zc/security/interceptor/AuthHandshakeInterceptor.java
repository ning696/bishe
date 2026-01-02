package cn.zc.security.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * WebSocket 握手认证拦截器，基于 JWT 鉴权并在会话中存储用户信息。
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final String QUERY_TOKEN_KEY = "token";
    private static final String ATTR_USER_ID = "wsUserId";
    private static final String ATTR_USER_KEY = "wsUserKey";
    private static final String ATTR_IDENTITY = "wsIdentity";

    private final TokenService tokenService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token-start:Bearer }")
    private String tokenStart;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = extractToken(request);
        if (StrUtil.isBlank(token)) {
            log.warn("WebSocket handshake failed: token is blank");
            return false;
        }

        Claims claims = tokenService.getClaims(token, jwtSecret);
        if (claims == null) {
            log.warn("WebSocket handshake failed: token invalid");
            return false;
        }

        Long userId = tokenService.getUserId(claims);
        String userKey = tokenService.getUserKey(claims);

        if (userId == null || StrUtil.isBlank(userKey)) {
            log.warn("WebSocket handshake failed: missing user info in token");
            return false;
        }

        LoginUser loginUser = tokenService.getLoginUser(token, jwtSecret);
        if (loginUser == null || loginUser.getIdentity() == null) {
            log.warn("WebSocket handshake failed: login user not found in cache, userKey={}", userKey);
            return false;
        }

        attributes.put(ATTR_USER_ID, userId);
        attributes.put(ATTR_USER_KEY, userKey);
        attributes.put(ATTR_IDENTITY, loginUser.getIdentity());

        // 刷新 token 过期时间
        tokenService.extendToken(claims);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // no-op
    }

    private String extractToken(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            String authorization = httpServletRequest.getHeader("Authorization");
            if (StrUtil.isNotBlank(authorization)) {
                return stripTokenPrefix(authorization);
            }

            String paramToken = httpServletRequest.getParameter(QUERY_TOKEN_KEY);
            if (StrUtil.isNotBlank(paramToken)) {
                return stripTokenPrefix(paramToken);
            }
        }

        String query = request.getURI().getQuery();
        if (StrUtil.isNotBlank(query)) {
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2 && Objects.equals(kv[0], QUERY_TOKEN_KEY)) {
                    return stripTokenPrefix(kv[1]);
                }
            }
        }
        return null;
    }

    private String stripTokenPrefix(String tokenValue) {
        String trimmed = tokenValue.trim();
        if (StrUtil.isBlank(tokenStart)) {
            return trimmed;
        }
        if (trimmed.startsWith(tokenStart)) {
            return trimmed.substring(tokenStart.length()).trim();
        }
        return trimmed;
    }

    public static Long getUserId(Map<String, Object> attributes) {
        Object value = attributes.get(ATTR_USER_ID);
        return value instanceof Long ? (Long) value : null;
    }

    public static String getUserKey(Map<String, Object> attributes) {
        Object value = attributes.get(ATTR_USER_KEY);
        return value instanceof String ? (String) value : null;
    }

    public static Integer getIdentity(Map<String, Object> attributes) {
        Object value = attributes.get(ATTR_IDENTITY);
        return value instanceof Integer ? (Integer) value : null;
    }
}


