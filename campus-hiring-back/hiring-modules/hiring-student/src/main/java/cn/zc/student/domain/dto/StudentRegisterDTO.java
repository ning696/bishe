package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生注册DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class StudentRegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}

