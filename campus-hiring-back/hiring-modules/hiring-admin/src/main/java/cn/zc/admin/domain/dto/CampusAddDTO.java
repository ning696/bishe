package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 校园新增DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class CampusAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 校园名称
     */
    private String campusName;

    /**
     * 校园编码
     */
    private String campusCode;

    /**
     * 校园地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}

