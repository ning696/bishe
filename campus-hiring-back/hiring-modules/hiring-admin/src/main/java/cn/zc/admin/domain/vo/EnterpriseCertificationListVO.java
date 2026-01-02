package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业认证列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseCertificationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String enterpriseName;
    private String legalPerson;
    private String phone;
    private Integer certificationStatus;
    private String certificationStatusName;
    private String certificationFile;
    private LocalDateTime createTime;
}

