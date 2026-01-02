package cn.zc.student.domain.vo;

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
    private Long enterpriseId;
    private String enterpriseName;
    private String enterpriseLogo;
    private Long jobId;
    private String jobName;
    private Boolean isOnline;
}

