package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送简历请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatResumeSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private Long resumeId;
}

