package cn.zc.api.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 职位详情VO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class JobDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职位ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 职位类别ID
     */
    private Long categoryId;

    /**
     * 职位类别名称
     */
    private String categoryName;

    /**
     * 职位名称
     */
    private String jobName;

    /**
     * 职位描述
     */
    private String jobDescription;

    /**
     * 工作地点
     */
    private String workLocation;

    /**
     * 最低薪资
     */
    private BigDecimal salaryMin;

    /**
     * 最高薪资
     */
    private BigDecimal salaryMax;

    /**
     * 薪资类型
     */
    private String salaryType;

    /**
     * 工作类型
     */
    private String jobType;

    /**
     * 招聘人数
     */
    private Integer recruitCount;

    /**
     * 状态：0-待审核，1-已通过，2-已拒绝，3-已下线
     */
    private Integer status;
}

