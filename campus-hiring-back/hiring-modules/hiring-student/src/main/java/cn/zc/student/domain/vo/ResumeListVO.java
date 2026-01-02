package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ResumeListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String resumeName;
    private String resumeFile;
    private Integer isDefault;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

