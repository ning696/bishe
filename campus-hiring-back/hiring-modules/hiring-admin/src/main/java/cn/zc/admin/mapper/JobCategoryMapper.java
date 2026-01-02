package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.JobCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职位类别Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobCategoryMapper extends BaseMapper<JobCategory> {
}

