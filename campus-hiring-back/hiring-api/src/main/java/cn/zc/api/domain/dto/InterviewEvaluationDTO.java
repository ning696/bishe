package cn.zc.api.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 面试评价DTO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewEvaluationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 面试ID
     */
    private Long interviewId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 职位ID
     */
    private Long jobId;

    /**
     * 专业能力评分（1-10分）
     */
    private Integer professionalAbility;

    /**
     * 沟通能力评分（1-10分）
     */
    private Integer communicationAbility;

    /**
     * 团队合作评分（1-10分）
     */
    private Integer teamCooperation;

    /**
     * 综合评分（1-10分）
     */
    private BigDecimal overallScore;

    /**
     * 评价内容
     */
    private String evaluationContent;

    /**
     * 优点
     */
    private String strengths;

    /**
     * 不足
     */
    private String weaknesses;

    /**
     * 推荐建议
     */
    private String recommendation;
}

