package cn.zc.common.mybatis;

import cn.zc.common.core.constants.Constants;
import cn.zc.common.core.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 元数据处理器
 * 自动填充创建时间、更新时间、创建人、更新人等字段
 * 
 * @author campus-hiring-system
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        
        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 填充创建人ID（从 ThreadLocal 获取，如果不存在则使用系统用户ID）
        Long userId = ThreadLocalUtil.get(Constants.USER_ID, Long.class);
        if (userId == null) {
            userId = Constants.SYSTEM_USER_ID;
        }
        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        
        // 填充更新人ID
        this.strictInsertFill(metaObject, "updateBy", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 填充更新人ID（从 ThreadLocal 获取，如果不存在则使用系统用户ID）
        Long userId = ThreadLocalUtil.get(Constants.USER_ID, Long.class);
        if (userId == null) {
            userId = Constants.SYSTEM_USER_ID;
        }
        this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
    }
}

















