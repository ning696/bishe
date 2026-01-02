package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送职位信息请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatJobSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private Long jobId;
}

