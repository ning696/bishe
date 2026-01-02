package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 处理面试申请DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewApplicationHandleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long applicationId;
    private Integer applicationStatus;
    private String handleRemark;
}

