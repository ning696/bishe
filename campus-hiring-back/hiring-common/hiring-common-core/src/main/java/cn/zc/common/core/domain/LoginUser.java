package cn.zc.common.core.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录用户信息
 * 用于存储用户登录后的身份信息，存储在 Redis 中
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
public class LoginUser {
    
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户身份
     * 1: 学生 (STUDENT)
     * 2: 企业 (ENTERPRISE)
     * 3: 管理员 (ADMIN)
     * 
     * @see cn.zc.common.core.enums.UserIdentity
     */
    private Integer identity;

    /**
     * 用户头像URL
     */
    private String headImage;
}
