package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 反馈处理DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class FeedbackHandleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 反馈ID
     */
    private Long feedbackId;

    /**
     * 处理状态：1-已处理，2-已关闭
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

