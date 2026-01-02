package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生信息VO（简要信息）
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;
    private String headImage;
}

