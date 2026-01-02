package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送验证码DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class SendCodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String phone;
}

