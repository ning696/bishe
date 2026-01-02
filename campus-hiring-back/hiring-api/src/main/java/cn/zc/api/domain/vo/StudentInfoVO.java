package cn.zc.api.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学生信息VO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String headImage;

    /**
     * 性别：0-女，1-男
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 所属校园ID
     */
    private Long campusId;

    /**
     * 校园名称
     */
    private String campusName;

    /**
     * 专业
     */
    private String major;

    /**
     * 学历
     */
    private String education;

    /**
     * 年级
     */
    private String grade;
}

