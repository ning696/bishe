package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}

