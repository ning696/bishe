package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建/获取会话请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatSessionCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long enterpriseId;
    private Long jobId;
}

