package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.CampusJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 校园职位关联Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface CampusJobMapper extends BaseMapper<CampusJob> {
}

