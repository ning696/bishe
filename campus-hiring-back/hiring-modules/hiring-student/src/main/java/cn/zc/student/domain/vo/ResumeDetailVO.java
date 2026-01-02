package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String resumeName;
    private String resumeFile;
    private String personalInfo;
    private String educationBackground;
    private String workExperience;
    private String projectExperience;
    private String skills;
    private String selfIntroduction;
    private Integer isDefault;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

