package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业认证审核DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseCertificationAuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 认证状态：1-已认证，3-认证失败
     */
    private Integer certificationStatus;

    /**
     * 审核备注
     */
    private String auditRemark;
}

