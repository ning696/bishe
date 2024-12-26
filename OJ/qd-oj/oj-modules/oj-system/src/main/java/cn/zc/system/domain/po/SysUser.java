package cn.zc.system.domain.po;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 张策
 * @since 2024-11-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("qd_sys_user")
public class SysUser extends BaseEntity {

//    private static final long serialVersionUID = 1L;

    /**
     * 用户id（主键）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    private String nickName;


    public SysUser(String userAccount, String password, String nickName) {
        this.userAccount = userAccount;
        this.password = password;
        this.nickName = nickName;
    }

    public SysUser(String userAccount, String password) {
        this.userAccount = userAccount;
        this.password = password;
    }

    public SysUser() {
    }
}
