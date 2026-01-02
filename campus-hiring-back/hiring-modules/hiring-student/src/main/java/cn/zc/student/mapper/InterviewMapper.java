package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Interview;
import cn.zc.student.domain.vo.InterviewDetailVO;
import cn.zc.student.domain.vo.InterviewListVO;
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
    List<InterviewListVO> selectInterviewList(@Param("studentId") Long studentId);

    /**
     * 分页查询面试/申请列表（可按状态过滤）
     */
    List<InterviewListVO> selectInterviewPage(@Param("studentId") Long studentId,
                                              @Param("status") Integer status);

    /**
     * 查询面试详情
     */
    InterviewDetailVO selectInterviewDetail(@Param("interviewId") Long interviewId,
                                            @Param("studentId") Long studentId);
}

