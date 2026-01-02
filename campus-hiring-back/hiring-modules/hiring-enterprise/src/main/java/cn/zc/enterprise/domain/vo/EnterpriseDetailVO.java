package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String enterpriseName;
    private String legalPerson;
    private String phone;
    private String email;
    private String logo;
    private String address;
    private String industry;
    private String scale;
    private String description;
    private String website;
    private Integer certificationStatus;
    private String certificationStatusName;
    private String certificationFile;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
}

