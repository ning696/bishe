package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 职位审核DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobAuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职位ID
     */
    private Long jobId;

    /**
     * 审核状态：1-已通过，2-已拒绝
     */
    private Integer status;

    /**
     * 审核备注
     */
    private String auditRemark;
}

