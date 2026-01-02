package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatMessageListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long sessionId;
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    private Integer senderType;
    private Long receiverId;
    private String receiverName;
    private String receiverAvatar;
    private Integer receiverType;
    private String messageType;
    private String content;
    private Long relatedJobId;
    private Integer isRead;
    private LocalDateTime readTime;
    private LocalDateTime createTime;
}

