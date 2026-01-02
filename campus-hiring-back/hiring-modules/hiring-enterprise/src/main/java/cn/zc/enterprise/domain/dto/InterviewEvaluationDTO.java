package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 面试评价DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewEvaluationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long interviewId;
    private Long studentId;
    private Long jobId;
    private Integer professionalAbility;
    private Integer communicationAbility;
    private Integer teamCooperation;
    private String evaluationContent;
    private String strengths;
    private String weaknesses;
    private String recommendation;
}

