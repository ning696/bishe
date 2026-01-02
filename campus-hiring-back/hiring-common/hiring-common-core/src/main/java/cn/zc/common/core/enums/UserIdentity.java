package cn.zc.common.core.enums;

import lombok.Getter;

/**
 * 用户身份枚举
 * 用于区分校园招聘系统中的不同用户角色
 * 
 * @author campus-hiring-system
 */
@Getter
public enum UserIdentity {

    /**
     * 学生用户
     * 可以浏览职位、投递简历、申请面试等
     */
    STUDENT(1, "学生"),

    /**
     * 企业用户
     * 可以发布职位、查看简历、安排面试等
     */
    ENTERPRISE(2, "企业"),

    /**
     * 管理员用户
     * 可以管理用户、审核职位、处理投诉等
     */
    ADMIN(3, "管理员");

    /**
     * 身份值（用于数据库存储）
     */
    private final Integer value;

    /**
     * 身份描述
     */
    private final String description;

    UserIdentity(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据值获取枚举
     * 
     * @param value 身份值
     * @return 用户身份枚举，如果不存在返回 null
     */
    public static UserIdentity getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserIdentity identity : values()) {
            if (identity.value.equals(value)) {
                return identity;
            }
        }
        return null;
    }

    /**
     * 判断是否为管理员
     * 
     * @return true 如果是管理员，否则返回 false
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * 判断是否为学生
     * 
     * @return true 如果是学生，否则返回 false
     */
    public boolean isStudent() {
        return this == STUDENT;
    }

    /**
     * 判断是否为企业
     * 
     * @return true 如果是企业，否则返回 false
     */
    public boolean isEnterprise() {
        return this == ENTERPRISE;
    }
}
