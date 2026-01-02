package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nickName;
    private String realName;
    private String phone;
    private String email;
    private String headImage;
    private Integer gender;
    private LocalDate birthday;
    private Long campusId;
    private String campusName;
    private String major;
    private String education;
    private String grade;
    private String skills;
    private Integer experience;
    private BigDecimal expectedSalary;
    private String expectedLocation;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
}

