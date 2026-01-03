package cn.zc.security.session;

import cn.zc.security.interceptor.AuthHandshakeInterceptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket 会话管理，维护用户与 STOMP 会话的映射关系。
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionManager {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key patterns
    private static final String SESSION_USER_KEY = "ws:session:user:%s";
    private static final String USER_SESSIONS_KEY = "ws:sessions:%d:%d";
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Map<String, Object> attributes = accessor.getSessionAttributes();
        if (sessionId == null || CollectionUtils.isEmpty(attributes)) {
            return;
        }
        Long userId = AuthHandshakeInterceptor.getUserId(attributes);
        Integer identity = AuthHandshakeInterceptor.getIdentity(attributes);
        String userKey = AuthHandshakeInterceptor.getUserKey(attributes);

        if (userId == null || identity == null) {
            log.warn("WebSocket connect missing user info, sessionId={}", sessionId);
            return;
        }
        registerSession(sessionId, new SessionUser(userId, identity, userKey));
        log.debug("WebSocket connected: sessionId={}, userId={}, identity={}", sessionId, userId, identity);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        if (sessionId == null) {
            return;
        }

        // 从Redis获取会话用户信息
        String sessionUserKey = String.format(SESSION_USER_KEY, sessionId);
        SessionUser sessionUser = (SessionUser) redisTemplate.opsForValue().get(sessionUserKey);
        if (sessionUser == null) {
            return;
        }

        // 从Redis中移除会话
        String userSessionsKey = String.format(USER_SESSIONS_KEY, sessionUser.getIdentity(), sessionUser.getUserId());
        redisTemplate.opsForSet().remove(userSessionsKey, sessionId);
        redisTemplate.delete(sessionUserKey);

        log.debug("WebSocket disconnected: sessionId={}, userId={}, identity={}", sessionId, sessionUser.getUserId(), sessionUser.getIdentity());
    }

    public Set<String> getSessionIds(Long userId, Integer identity) {
        String userSessionsKey = String.format(USER_SESSIONS_KEY, identity, userId);
        Set<Object> sessions = redisTemplate.opsForSet().members(userSessionsKey);
        if (sessions == null || sessions.isEmpty()) {
            return Collections.emptySet();
        }
        return sessions.stream()
            .map(Object::toString)
            .collect(java.util.stream.Collectors.toSet());
    }

    private void registerSession(String sessionId, SessionUser sessionUser) {
        // 存储会话用户信息
        String sessionUserKey = String.format(SESSION_USER_KEY, sessionId);
        redisTemplate.opsForValue().set(sessionUserKey, sessionUser, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);

        // 存储用户会话集合
        String userSessionsKey = String.format(USER_SESSIONS_KEY, sessionUser.getIdentity(), sessionUser.getUserId());
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        // 设置过期时间
        redisTemplate.expire(userSessionsKey, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    @Getter
    @RequiredArgsConstructor
    public static class SessionUser {
        private final Long userId;
        private final Integer identity;
        private final String userKey;
    }
}


