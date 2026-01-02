package cn.zc.common.core.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 * 用于标识用户在校园招聘系统中的状态
 * 
 * @author campus-hiring-system
 */
@Getter
public enum UserStatus {

    /**
     * 已拉黑
     * 用户被管理员拉黑，无法使用系统功能
     */
    BLOCKED(0, "已拉黑"),

    /**
     * 正常
     * 用户状态正常，可以正常使用系统
     */
    NORMAL(1, "正常"),

    /**
     * 已禁用
     * 用户被管理员禁用，暂时无法使用系统功能
     */
    DISABLED(2, "已禁用"),

    /**
     * 待审核
     * 用户注册后等待管理员审核（主要用于企业用户）
     */
    PENDING(3, "待审核");

    /**
     * 状态值（用于数据库存储）
     */
    private final Integer value;

    /**
     * 状态描述
     */
    private final String description;

    UserStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据值获取枚举
     * 
     * @param value 状态值
     * @return 用户状态枚举，如果不存在返回 null
     */
    public static UserStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为正常状态
     * 
     * @return true 如果是正常状态，否则返回 false
     */
    public boolean isNormal() {
        return this == NORMAL;
    }

    /**
     * 判断是否为可用状态（正常或待审核）
     * 
     * @return true 如果是可用状态，否则返回 false
     */
    public boolean isAvailable() {
        return this == NORMAL || this == PENDING;
    }

    /**
     * 判断是否为不可用状态（拉黑或禁用）
     * 
     * @return true 如果是不可用状态，否则返回 false
     */
    public boolean isUnavailable() {
        return this == BLOCKED || this == DISABLED;
    }
}
