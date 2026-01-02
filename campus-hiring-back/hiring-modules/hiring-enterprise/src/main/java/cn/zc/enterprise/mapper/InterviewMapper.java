package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.Interview;
import cn.zc.enterprise.domain.vo.InterviewListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 面试Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {

    /**
     * 查询面试列表
     */
    List<InterviewListVO> selectInterviewList(@Param("enterpriseId") Long enterpriseId,
                                             @Param("jobId") Long jobId,
                                             @Param("interviewStatus") Integer interviewStatus);
}











