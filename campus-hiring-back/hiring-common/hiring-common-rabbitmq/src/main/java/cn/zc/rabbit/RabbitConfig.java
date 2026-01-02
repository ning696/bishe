package cn.zc.rabbit;

import cn.zc.common.core.constants.RabbitMQConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * 定义消息队列和消息转换器
 * 
 * @author campus-hiring-system
 */
@Configuration
public class RabbitConfig {

    /**
     * 简历投递通知队列
     */
    @Bean
    public Queue resumeDeliveryQueue() {
        return new Queue(RabbitMQConstants.RESUME_DELIVERY_QUEUE, true);
    }

    /**
     * 消息通知队列
     */
    @Bean
    public Queue messageNotifyQueue() {
        return new Queue(RabbitMQConstants.MESSAGE_NOTIFY_QUEUE, true);
    }

    /**
     * 聊天消息存储队列
     */
    @Bean
    public Queue chatMessageStoreQueue() {
        return new Queue(RabbitMQConstants.CHAT_MESSAGE_STORE_QUEUE, true);
    }

    /**
     * 聊天消息推送队列
     */
    @Bean
    public Queue chatMessagePushQueue() {
        return new Queue(RabbitMQConstants.CHAT_MESSAGE_PUSH_QUEUE, true);
    }

    /**
     * 消息转换器
     * 使用 JSON 格式序列化消息
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}




