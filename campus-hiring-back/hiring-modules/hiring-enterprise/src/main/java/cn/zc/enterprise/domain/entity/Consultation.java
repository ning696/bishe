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
 * 咨询实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("consultation")
public class Consultation extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;
    private Long enterpriseId;
    private Long jobId;
    private String consultationType;
    private String title;
    private String content;
    private String replyContent;
    private LocalDateTime replyTime;
    private Integer status;
}

