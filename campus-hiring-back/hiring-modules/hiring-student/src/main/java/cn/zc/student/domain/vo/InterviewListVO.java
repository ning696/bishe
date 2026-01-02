package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 面试列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long jobId;
    private String jobName;
    private Long enterpriseId;
    private String enterpriseName;
    private Integer interviewStatus;
    private String interviewStatusName;
    private LocalDateTime interviewTime;
    private String interviewLocation;
    private String interviewType;
    private String remark;
    private LocalDateTime createTime;
}

