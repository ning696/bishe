package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业信息更新DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String enterpriseName;
    private String legalPerson;
    private String phone;
    private String email;
    private String address;
    private String industry;
    private String scale;
    private String description;
    private String website;
}

