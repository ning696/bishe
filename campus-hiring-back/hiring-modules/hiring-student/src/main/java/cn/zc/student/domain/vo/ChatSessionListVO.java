package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatSessionListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private String enterpriseLogo;
    private Long jobId;
    private String jobName;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
    private Boolean isOnline;
}

