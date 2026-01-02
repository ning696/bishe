package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 面试申请DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewApplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobId;
    private Long resumeId;
}

