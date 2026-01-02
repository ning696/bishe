package cn.zc.enterprise.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 聊天会话实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("chat_session")
public class ChatSession extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long studentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long enterpriseId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long jobId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long lastMessageId;

    private LocalDateTime lastMessageTime;

    private String lastMessageContent;

    private Integer studentUnreadCount;

    private Integer enterpriseUnreadCount;

    private Integer status;
}

