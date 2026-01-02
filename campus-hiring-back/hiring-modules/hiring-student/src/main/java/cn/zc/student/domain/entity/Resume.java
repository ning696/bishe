package cn.zc.student.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

/**
 * 简历实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("resume")
public class Resume extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;
    private String resumeName;
    private String resumeFile;
    private String personalInfo;
    private String educationBackground;
    private String workExperience;
    private String projectExperience;
    private String skills;
    private String selfIntroduction;
    private Integer isDefault;
    private Integer status;
}

