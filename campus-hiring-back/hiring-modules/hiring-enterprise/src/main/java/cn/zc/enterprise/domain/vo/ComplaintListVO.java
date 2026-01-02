package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投诉列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ComplaintListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private String studentName;
    private Long jobId;
    private String jobName;
    private String title;
    private String content;
    private Integer handleStatus;
    private String handleStatusName;
    private String handleResult;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;
}

