package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业认证申请DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class CertificationApplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String certificationFile;
}

