package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业状态更新 DTO
 *
 * @author
 */
@Data
public class EnterpriseStatusUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业 ID
     */
    private Long enterpriseId;

    /**
     * 状态：0-已拉黑，1-正常，2-已禁用，3-待审核
     */
    private Integer status;
}

