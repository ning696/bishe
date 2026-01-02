package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 职位列表VO（企业端）
 * 
 * @author campus-hiring-system
 */
@Data
public class JobListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String jobName;
    private String workLocation;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryType;
    private Integer recruitCount;
    private Integer viewCount;
    private Integer applyCount;
    private Integer status;
    private String statusName;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
}

