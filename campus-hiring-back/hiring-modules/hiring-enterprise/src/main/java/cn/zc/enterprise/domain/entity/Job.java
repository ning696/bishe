package cn.zc.enterprise.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 职位实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("job")
public class Job extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long enterpriseId;
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
    private Integer viewCount;
    private Integer applyCount;
    private Integer status;
    private String auditRemark;
    private LocalDateTime auditTime;
    private Long auditBy;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
}

