package cn.zc.student.domain.entity;

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
 * 简历投递实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("resume_delivery")
public class ResumeDelivery extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;
    private Long enterpriseId;
    private Long jobId;
    private Long resumeId;
    private Integer deliveryStatus;
    private LocalDateTime deliveryTime;
    private LocalDateTime viewTime;
    private LocalDateTime handleTime;
    private String handleRemark;
}

