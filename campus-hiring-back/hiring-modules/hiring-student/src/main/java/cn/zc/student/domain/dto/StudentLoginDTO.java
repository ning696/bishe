package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生登录DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}

