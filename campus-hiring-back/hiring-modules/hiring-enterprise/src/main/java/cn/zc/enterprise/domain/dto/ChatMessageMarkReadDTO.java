package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 标记已读请求DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatMessageMarkReadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private List<Long> messageIds;
}

