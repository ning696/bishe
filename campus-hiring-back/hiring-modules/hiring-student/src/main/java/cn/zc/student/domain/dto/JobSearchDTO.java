package cn.zc.student.domain.dto;

import cn.zc.common.core.domain.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 职位搜索DTO
 * 
 * @author campus-hiring-system
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JobSearchDTO extends PageQueryDTO {

    private String jobName; // 职位名称（模糊搜索）
    private Long categoryId; // 职位类别ID
    private String workLocation; // 工作地点
    private String jobType; // 工作类型：全职、兼职、实习
    private String requiredEducation; // 要求学历
    private BigDecimal salaryMin; // 最低薪资
    private BigDecimal salaryMax; // 最高薪资
    private String sortField; // 排序字段：publishTime、salary、viewCount
    private String sortOrder; // 排序方式：asc、desc
}

