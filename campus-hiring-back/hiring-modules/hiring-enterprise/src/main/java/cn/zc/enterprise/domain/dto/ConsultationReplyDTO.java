package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 回复咨询DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ConsultationReplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long consultationId;
    private String replyContent;
}

