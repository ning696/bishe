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
 * 投诉实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("complaint")
public class Complaint extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer complaintType;
    private Long complainerId;
    private Integer complainerType;
    private Long complainedId;
    private Integer complainedType;
    private Long jobId;
    private String title;
    private String content;
    private String attachment;
    private Integer handleStatus;
    private String handleResult;
    private String handleRemark;
    private LocalDateTime handleTime;
    private Long handleBy;
}

