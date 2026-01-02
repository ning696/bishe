package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历申请列表VO（企业端简历管理使用）
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeApplicationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentPhone;
    private Long jobId;
    private String jobName;
    private Long resumeId;
    private String resumeName;
    private String resumeFile;
    private Integer applicationStatus;
    private String applicationStatusName;
    private LocalDateTime applicationTime;
}

