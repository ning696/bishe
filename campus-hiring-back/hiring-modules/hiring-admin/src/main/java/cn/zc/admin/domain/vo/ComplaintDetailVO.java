package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投诉详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ComplaintDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer complaintType;
    private String complaintTypeName;
    private Long complainerId;
    private String complainerName;
    private Long complainedId;
    private String complainedName;
    private Long jobId;
    private String jobName;
    private String title;
    private String content;
    private String attachment;
    private Integer handleStatus;
    private String handleStatusName;
    private String handleResult;
    private String handleRemark;
    private LocalDateTime handleTime;
    private Long handleBy;
    private LocalDateTime createTime;
}

