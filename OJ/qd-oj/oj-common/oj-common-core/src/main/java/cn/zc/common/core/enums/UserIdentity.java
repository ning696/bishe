package cn.zc.common.core.enums;

import lombok.Getter;

@Getter
public enum UserIdentity {
    ORDINARY(1, "普通用户"),
    ADMIN(2, "管理员");
    private Integer value;
    private String das;
    UserIdentity(int value, String das) {
        this.value = value;
        this.das = das;
    }
}
