package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新简历DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long resumeId;
    private String resumeName;
    private String resumeFile;
    private String personalInfo;
    private String educationBackground;
    private String workExperience;
    private String projectExperience;
    private String skills;
    private String selfIntroduction;
    private Integer isDefault;
}

