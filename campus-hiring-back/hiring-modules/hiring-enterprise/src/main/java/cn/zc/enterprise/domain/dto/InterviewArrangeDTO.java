package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 安排面试DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class InterviewArrangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long applicationId;
    private Long studentId;
    private Long jobId;
    private String interviewTime;
    private String interviewLocation;
    private String interviewType;
    private String contactPerson;
    private String contactPhone;
    private String remark;
}

