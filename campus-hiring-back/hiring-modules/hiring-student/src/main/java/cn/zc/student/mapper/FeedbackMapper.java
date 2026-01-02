package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 反馈Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}

