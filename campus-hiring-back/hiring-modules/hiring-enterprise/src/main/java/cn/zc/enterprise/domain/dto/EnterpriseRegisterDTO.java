package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业注册DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseRegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String enterpriseName;
}

