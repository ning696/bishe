package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 面试申请列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobApplicationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentPhone;
    private Long jobId;
    private String jobName;
    private Long resumeId;
    private Integer applicationStatus;
    private String applicationStatusName;
    private LocalDateTime applicationTime;
}

