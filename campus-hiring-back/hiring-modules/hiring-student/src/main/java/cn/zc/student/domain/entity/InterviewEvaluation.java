package cn.zc.student.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 面试评价实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("interview_evaluation")
public class InterviewEvaluation extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long interviewId;
    private Long studentId;
    private Long enterpriseId;
    private Long jobId;
    private Integer professionalAbility;
    private Integer communicationAbility;
    private Integer teamCooperation;
    private BigDecimal overallScore;
    private String evaluationContent;
    private String strengths;
    private String weaknesses;
    private String recommendation;
}

