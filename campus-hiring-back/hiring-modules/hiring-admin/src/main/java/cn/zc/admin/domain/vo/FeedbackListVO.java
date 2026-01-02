package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 反馈列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class FeedbackListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Integer userType;
    private String userName;
    private String feedbackType;
    private String title;
    private String content;
    private Integer handleStatus;
    private String handleStatusName;
    private LocalDateTime createTime;
}

