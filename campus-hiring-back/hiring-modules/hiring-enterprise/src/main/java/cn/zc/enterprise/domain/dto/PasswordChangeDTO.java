package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 密码修改DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class PasswordChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String oldPassword;
    private String newPassword;
}

