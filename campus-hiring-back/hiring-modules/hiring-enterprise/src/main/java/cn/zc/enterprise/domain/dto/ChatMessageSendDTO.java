package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送消息请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatMessageSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private String messageType;
    private String content;
    private Long relatedJobId;
}

