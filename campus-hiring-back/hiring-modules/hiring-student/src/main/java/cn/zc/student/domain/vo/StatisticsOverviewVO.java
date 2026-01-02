package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生统计概览VO
 * 
 * @author campus-hiring-system
 */
@Data
public class StatisticsOverviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总投递数
     */
    private Integer totalApplications;

    /**
     * 待处理投递数
     */
    private Integer pendingApplications;

    /**
     * 已通过投递数
     */
    private Integer passedApplications;

    /**
     * 总面试数
     */
    private Integer totalInterviews;

    /**
     * 已安排面试数
     */
    private Integer scheduledInterviews;

    /**
     * 已完成面试数
     */
    private Integer completedInterviews;

    /**
     * 总收藏数
     */
    private Integer totalFavorites;

    /**
     * 总简历数
     */
    private Integer totalResumes;

    /**
     * 默认简历ID
     */
    private Long defaultResumeId;

    /**
     * 简历完整度（百分比，0-100）
     */
    private Integer resumeCompleteness;

    /**
     * 头像
     */
    private String headImage;
}


