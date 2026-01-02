package cn.zc.api.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 简历详情VO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历ID
     */
    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 简历名称
     */
    private String resumeName;

    /**
     * 简历文件URL
     */
    private String resumeFile;

    /**
     * 个人信息（JSON格式）
     */
    private String personalInfo;

    /**
     * 教育背景（JSON格式）
     */
    private String educationBackground;

    /**
     * 工作经历（JSON格式）
     */
    private String workExperience;

    /**
     * 项目经历（JSON格式）
     */
    private String projectExperience;

    /**
     * 技能描述
     */
    private String skills;

    /**
     * 自我介绍
     */
    private String selfIntroduction;

    /**
     * 学生姓名（企业端查看时使用）
     */
    private String studentName;

    /**
     * 学生手机号（企业端查看时使用）
     */
    private String studentPhone;

    /**
     * 学生邮箱（企业端查看时使用）
     */
    private String studentEmail;

    /**
     * 投递状态（企业端查看时使用）
     */
    private Integer deliveryStatus;

    /**
     * 投递状态名称（企业端查看时使用）
     */
    private String deliveryStatusName;

    /**
     * 查看时间（企业端查看时使用）
     */
    private java.time.LocalDateTime viewTime;
}

