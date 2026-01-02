package cn.zc.api.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息VO（服务间传输）
 * 
 * @author campus-hiring-system
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
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
     * 用户类型：1-学生，2-企业
     */
    private Integer userType;

    /**
     * 状态：0-已拉黑，1-正常，2-已禁用，3-待审核
     */
    private Integer status;
}

