package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 面试评价VO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewEvaluationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long interviewId;
    private Integer professionalAbility;
    private Integer communicationAbility;
    private Integer teamCooperation;
    private java.math.BigDecimal overallScore;
    private String evaluationContent;
    private String strengths;
    private String weaknesses;
    private String recommendation;
    private LocalDateTime createTime;
}

