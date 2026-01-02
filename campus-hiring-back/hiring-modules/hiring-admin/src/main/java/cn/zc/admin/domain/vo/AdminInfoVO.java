package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员信息VO
 * 
 * @author campus-hiring-system
 */
@Data
public class AdminInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String headImage;

    /**
     * 状态：0-已拉黑，1-正常，2-已禁用
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;
}

