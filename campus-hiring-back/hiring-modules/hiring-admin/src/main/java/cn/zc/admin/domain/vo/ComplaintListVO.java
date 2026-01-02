package cn.zc.admin.domain.vo;

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
    private Integer complaintType;
    private String complaintTypeName;
    private Long complainerId;
    private String complainerName;
    private Long complainedId;
    private String complainedName;
    private String title;
    private String content;
    private Integer handleStatus;
    private String handleStatusName;
    private LocalDateTime createTime;
}

