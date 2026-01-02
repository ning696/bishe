package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 提交投诉DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ComplaintAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long studentId;
    private Long jobId;
    private String title;
    private String content;
    private String attachment;
}

