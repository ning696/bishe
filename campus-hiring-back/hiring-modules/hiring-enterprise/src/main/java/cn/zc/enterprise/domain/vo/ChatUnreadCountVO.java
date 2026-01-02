package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 未读消息数VO
 * 
 * @author campus-hiring-system
 */
@Data
public class ChatUnreadCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalUnreadCount;
}

