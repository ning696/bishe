package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 校园列表VO
 * 
 * @author campus-hiring-system
 */
@Data
public class CampusListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String campusName;
    private String campusCode;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String statusName;
}

