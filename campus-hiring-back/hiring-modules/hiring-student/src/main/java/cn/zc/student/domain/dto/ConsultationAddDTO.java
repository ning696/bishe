package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发起咨询DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ConsultationAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long enterpriseId;
    private Long jobId;
    private String consultationType;
    private String title;
    private String content;
}

