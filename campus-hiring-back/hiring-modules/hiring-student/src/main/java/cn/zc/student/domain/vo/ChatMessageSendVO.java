package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 发送消息响应VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatMessageSendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long messageId;
    private Long sessionId;
    private Long senderId;
    private Integer senderType;
    private String senderName;
    private String senderAvatar;
    private Long receiverId;
    private Integer receiverType;
    private String receiverName;
    private String receiverAvatar;
    private String messageType;
    private String content;
    private Long relatedJobId;
    private Boolean isRead;
    private LocalDateTime createTime;
}

