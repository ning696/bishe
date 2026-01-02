package cn.zc.student.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 学生实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("student")
public class Student extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String nickName;
    private String realName;
    private String phone;
    private String email;
    private String headImage;
    private Integer gender;
    private LocalDate birthday;
    private Long campusId;
    private String major;
    private String education;
    private String grade;
    private String skills;
    private Integer experience;
    private BigDecimal expectedSalary;
    private String expectedLocation;
    private Integer status;
    private String remark;
}

