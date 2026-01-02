package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建简历DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

