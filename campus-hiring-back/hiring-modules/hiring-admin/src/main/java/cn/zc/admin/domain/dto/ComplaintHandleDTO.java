package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 投诉处理DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ComplaintHandleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉ID
     */
    private Long complaintId;

    /**
     * 处理状态：1-处理中，2-已处理，3-已关闭
     */
    private Integer handleStatus;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理备注
     */
    private String handleRemark;
}

