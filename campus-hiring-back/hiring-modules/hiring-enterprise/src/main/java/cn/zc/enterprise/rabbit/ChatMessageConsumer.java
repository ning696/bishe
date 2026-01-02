package cn.zc.enterprise.rabbit;

import cn.zc.common.core.constants.RabbitMQConstants;
import cn.zc.common.core.domain.vo.ChatMessageVO;
import cn.zc.common.core.domain.vo.WebSocketMessage;
import cn.zc.enterprise.domain.dto.ChatMessageStoreDTO;
import cn.zc.enterprise.domain.entity.ChatMessage;
import cn.zc.enterprise.domain.entity.ChatSession;
import cn.zc.enterprise.mapper.ChatMessageMapper;
import cn.zc.enterprise.mapper.ChatSessionMapper;
import cn.zc.security.service.ChatMessagePushService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 聊天消息消费者
 * 异步处理消息存储、更新会话信息、更新未读消息数
 * 
 * @author campus-hiring-system
 */
@Component
@Slf4j
public class ChatMessageConsumer {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private ChatMessagePushService chatMessagePushService;

    @RabbitListener(queues = RabbitMQConstants.CHAT_MESSAGE_STORE_QUEUE)
    @Transactional
    public void consume(ChatMessageStoreDTO messageDTO) {
        try {
            // 1. 保存消息到数据库
            ChatMessage message = new ChatMessage();
            message.setSessionId(messageDTO.getSessionId());
            message.setSenderId(messageDTO.getSenderId());
            message.setSenderType(messageDTO.getSenderType());
            message.setReceiverId(messageDTO.getReceiverId());
            message.setReceiverType(messageDTO.getReceiverType());
            message.setMessageType(messageDTO.getMessageType());
            message.setContent(messageDTO.getContent());
            message.setRelatedJobId(messageDTO.getRelatedJobId());
            message.setIsRead(0); // 未读
            LocalDateTime now = LocalDateTime.now();
            message.setCreateTime(now);

            int rows = chatMessageMapper.insert(message);
            if (rows <= 0) {
                log.error("保存消息失败: {}", messageDTO);
                return;
            }

            // 2. 更新会话的最后消息信息
            String lastMessageContent = messageDTO.getContent();
            if (StringUtils.hasText(lastMessageContent) && lastMessageContent.length() > 50) {
                lastMessageContent = lastMessageContent.substring(0, 50) + "...";
            }

            LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ChatSession::getId, messageDTO.getSessionId())
                    .set(ChatSession::getLastMessageId, message.getId())
                    .set(ChatSession::getLastMessageTime, LocalDateTime.now())
                    .set(ChatSession::getLastMessageContent, lastMessageContent);

            // 3. 更新未读消息数（根据接收者类型）
            if (messageDTO.getReceiverType() == 1) {
                // 接收者是学生
                updateWrapper.setSql("student_unread_count = student_unread_count + 1");
            } else if (messageDTO.getReceiverType() == 2) {
                // 接收者是企业
                updateWrapper.setSql("enterprise_unread_count = enterprise_unread_count + 1");
            }

            chatSessionMapper.update(null, updateWrapper);

            // 4. 推送 WebSocket 消息给接收方
            ChatMessageVO messageVO = buildMessageVO(message, now);
            WebSocketMessage<ChatMessageVO> payload = WebSocketMessage.of("message", messageVO);
            chatMessagePushService.pushChatMessage(messageDTO.getReceiverId(), messageDTO.getReceiverType(), payload);

            log.info("消息存储成功: messageId={}, sessionId={}", message.getId(), messageDTO.getSessionId());
        } catch (Exception e) {
            log.error("消费消息异常: {}", messageDTO, e);
            // 这里可以根据业务需求决定是否重试或记录到死信队列
        }
    }

    private ChatMessageVO buildMessageVO(ChatMessage message, LocalDateTime createTime) {
        ChatMessageVO vo = new ChatMessageVO();
        vo.setMessageId(message.getId());
        vo.setSessionId(message.getSessionId());
        vo.setSenderId(message.getSenderId());
        vo.setSenderType(message.getSenderType());
        vo.setReceiverId(message.getReceiverId());
        vo.setReceiverType(message.getReceiverType());
        vo.setMessageType(message.getMessageType());
        vo.setContent(message.getContent());
        vo.setRelatedJobId(message.getRelatedJobId());
        vo.setIsRead(Boolean.FALSE);
        vo.setCreateTime(createTime);
        return vo;
    }
}

