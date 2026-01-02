package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 职位列表VO（学生端）
 * 
 * @author campus-hiring-system
 */
@Data
public class JobListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private String jobName;
    private String workLocation;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryType;
    private String jobType;
    private Integer recruitCount;
    private Integer viewCount;
    private Integer applyCount;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private Boolean isFavorite; // 是否已收藏
    private LocalDateTime favoriteTime; // 收藏时间（仅用于收藏列表）
    private BigDecimal matchScore; // 职位与学生匹配度
}

