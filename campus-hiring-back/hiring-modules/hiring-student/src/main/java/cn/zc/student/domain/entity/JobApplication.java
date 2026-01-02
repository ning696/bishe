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
 * 职位申请实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("job_application")
public class JobApplication extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;
    private Long jobId;
    private Long resumeId;
    private Integer applicationStatus;
    private LocalDateTime applicationTime;
    private LocalDateTime handleTime;
    private String handleRemark;
}

