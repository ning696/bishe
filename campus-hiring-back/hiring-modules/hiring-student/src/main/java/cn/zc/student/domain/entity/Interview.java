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
 * 面试实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("interview")
public class Interview extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;
    private Long enterpriseId;
    private Long jobId;
    private Long applicationId;
    private LocalDateTime interviewTime;
    private String interviewLocation;
    private String interviewType;
    private Integer interviewStatus;
    private String contactPerson;
    private String contactPhone;
    private String remark;
}

