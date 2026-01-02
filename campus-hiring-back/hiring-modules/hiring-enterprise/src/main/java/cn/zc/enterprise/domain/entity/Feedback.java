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
 * 反馈实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("feedback")
public class Feedback extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Integer userType;

    private String feedbackType;

    private String title;

    private String content;

    private String contactInfo;

    private Integer handleStatus;

    private String handleResult;

    private String handleRemark;

    private LocalDateTime handleTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long handleBy;
}

