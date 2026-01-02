package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 简历投递DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeDeliveryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobId;
    private Long resumeId;
}

