package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 学生信息更新DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;
    private String realName;
    private String phone;
    private String email;
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
}

