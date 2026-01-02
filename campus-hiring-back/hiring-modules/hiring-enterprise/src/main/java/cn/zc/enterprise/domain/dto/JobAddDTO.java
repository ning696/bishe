package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 发布职位DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;
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
    private String expireTime;
    private List<Long> campusIds;
}

