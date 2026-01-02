package cn.zc.student.domain.vo;

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
    private Long enterpriseId;
    private String enterpriseName;
    private Long jobId;
    private String jobName;
    private String title;
    private String content;
    private String attachment;
    private String handleResult;
    private String handleRemark;
    private LocalDateTime handleTime;
    private Integer handleStatus;
    private String handleStatusName;
    private LocalDateTime createTime;
}

