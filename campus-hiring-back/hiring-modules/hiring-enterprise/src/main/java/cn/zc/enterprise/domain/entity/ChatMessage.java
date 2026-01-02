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
 * 聊天消息实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("chat_message")
public class ChatMessage extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;

    private Integer senderType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;

    private Integer receiverType;

    private String messageType;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long relatedJobId;

    private Integer isRead;

    private LocalDateTime readTime;

    private LocalDateTime createTime;
}

