package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业信息VO（简要信息）
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String enterpriseName;
    private String logo;
}

