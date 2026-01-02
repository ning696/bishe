package cn.zc.api.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业信息VO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class EnterpriseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 法人代表
     */
    private String legalPerson;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 企业Logo URL
     */
    private String logo;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 企业简介
     */
    private String description;

    /**
     * 企业官网
     */
    private String website;
}

