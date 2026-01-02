package cn.zc.student.rabbit;

import cn.zc.common.core.constants.RabbitMQConstants;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.exception.ServiceException;
import cn.zc.student.domain.dto.ChatMessageStoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聊天消息生产者
 * 
 * @author campus-hiring-system
 */
@Component
@Slf4j
public class ChatMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到RabbitMQ队列（异步存储）
     */
    public void sendMessage(ChatMessageStoreDTO messageDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstants.CHAT_MESSAGE_STORE_QUEUE, messageDTO);
        } catch (Exception e) {
            log.error("生产者发送消息异常", e);
            throw new ServiceException(ResultCode.FAILED_RABBIT_PRODUCE);
        }
    }
}