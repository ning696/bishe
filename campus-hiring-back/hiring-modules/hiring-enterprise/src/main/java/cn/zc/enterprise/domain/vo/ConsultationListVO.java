package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 咨询列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ConsultationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String studentName;
    private Long jobId;
    private String jobName;
    private String consultationType;
    private String title;
    private String content;
    private String replyContent;
    private LocalDateTime replyTime;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
}

