package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业登录DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}

