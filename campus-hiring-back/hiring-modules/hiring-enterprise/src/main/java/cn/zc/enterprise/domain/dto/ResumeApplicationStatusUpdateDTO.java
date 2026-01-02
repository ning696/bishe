package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 简历申请状态更新DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeApplicationStatusUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long applicationId;
    private Integer applicationStatus;
    private String handleRemark;
}

