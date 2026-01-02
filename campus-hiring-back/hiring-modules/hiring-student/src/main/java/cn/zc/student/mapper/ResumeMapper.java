package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Resume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 简历Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
}

