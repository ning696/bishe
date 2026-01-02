package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 提交反馈请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class FeedbackAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String feedbackType;
    private String title;
    private String content;
    private String contactInfo;
}

