package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 校园详情VO
 * 
 * @author campus-hiring-system
 */
@Data
public class CampusDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String campusName;
    private String campusCode;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String statusName;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
}

