package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 聊天消息存储DTO（用于RabbitMQ消息传递）
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatMessageStoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private Long senderId;
    private Integer senderType;
    private Long receiverId;
    private Integer receiverType;
    private String messageType;
    private String content;
    private Long relatedJobId;
}

