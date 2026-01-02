package cn.zc.api.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 咨询DTO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class ConsultationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 咨询ID
     */
    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 职位ID
     */
    private Long jobId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 状态：0-待回复，1-已回复，2-已关闭
     */
    private Integer status;
}

