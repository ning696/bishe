package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 职位详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private Long categoryId;
    private String categoryName;
    private String jobName;
    private String jobDescription;
    private String requiredMajor;
    private String requiredSkills;
    private String requiredEducation;
    private Integer requiredExperience;
    private String workLocation;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryType;
    private String jobType;
    private Integer recruitCount;
    private Integer viewCount;
    private Integer applyCount;
    private Integer status;
    private String statusName;
    private String auditRemark;
    private LocalDateTime auditTime;
    private Long auditBy;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
}

