package cn.zc.security.service;

import cn.zc.common.core.domain.vo.WebSocketMessage;
import cn.zc.security.session.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * WebSocket 聊天消息推送服务。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessagePushService {

    private static final String CHAT_QUEUE_PATTERN = "/queue/chat/%d/%d";

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    public <T> void pushChatMessage(Long targetUserId, Integer targetIdentity, WebSocketMessage<T> payload) {
        if (targetUserId == null || targetIdentity == null) {
            return;
        }
        Set<String> sessionIds = sessionManager.getSessionIds(targetUserId, targetIdentity);
        if (sessionIds.isEmpty()) {
            log.debug("WebSocket push skipped: user offline, userId={}, identity={}", targetUserId, targetIdentity);
            return;
        }
        String destination = CHAT_QUEUE_PATTERN.formatted(targetIdentity, targetUserId);
        messagingTemplate.convertAndSend(destination, payload);
        log.debug("WebSocket push sent: destination={}, sessions={}", destination, sessionIds);
    }
}


