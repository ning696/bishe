package cn.zc.student.mapper;

import cn.zc.student.domain.entity.JobFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职位收藏Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobFavoriteMapper extends BaseMapper<JobFavorite> {
}

