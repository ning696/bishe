package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会话详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatSessionDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentAvatar;
    private Long jobId;
    private String jobName;
    private Boolean isOnline;
}

