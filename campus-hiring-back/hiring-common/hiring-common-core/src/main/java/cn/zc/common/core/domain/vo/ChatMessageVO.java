package cn.zc.common.core.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * WebSocket 推送用的聊天消息视图对象。
 */
@Data
public class ChatMessageVO implements Serializable {

    @Serial
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


