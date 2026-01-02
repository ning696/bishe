package cn.zc.security.session;

import cn.zc.security.interceptor.AuthHandshakeInterceptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 会话管理，维护用户与 STOMP 会话的映射关系。
 */
@Component
@Slf4j
public class WebSocketSessionManager {

    private final Map<String, SessionUser> sessionMap = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Long, Set<String>>> identitySessionMap = new ConcurrentHashMap<>();

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
        SessionUser sessionUser = sessionMap.remove(sessionId);
        if (sessionUser == null) {
            return;
        }
        Map<Long, Set<String>> userSessions = identitySessionMap.get(sessionUser.getIdentity());
        if (userSessions != null) {
            Set<String> sessions = userSessions.get(sessionUser.getUserId());
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    userSessions.remove(sessionUser.getUserId());
                }
            }
        }
        log.debug("WebSocket disconnected: sessionId={}, userId={}, identity={}", sessionId, sessionUser.getUserId(), sessionUser.getIdentity());
    }

    public Set<String> getSessionIds(Long userId, Integer identity) {
        Map<Long, Set<String>> userSessions = identitySessionMap.get(identity);
        if (userSessions == null) {
            return Collections.emptySet();
        }
        Set<String> sessions = userSessions.get(userId);
        return sessions == null ? Collections.emptySet() : Set.copyOf(sessions);
    }

    private void registerSession(String sessionId, SessionUser sessionUser) {
        sessionMap.put(sessionId, sessionUser);
        identitySessionMap.computeIfAbsent(sessionUser.getIdentity(), key -> new ConcurrentHashMap<>())
            .computeIfAbsent(sessionUser.getUserId(), key -> new CopyOnWriteArraySet<>())
            .add(sessionId);
    }

    @Getter
    @RequiredArgsConstructor
    public static class SessionUser {
        private final Long userId;
        private final Integer identity;
        private final String userKey;
    }
}


