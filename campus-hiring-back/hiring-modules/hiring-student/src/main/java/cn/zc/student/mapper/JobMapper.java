package cn.zc.student.mapper;

import cn.zc.student.domain.dto.JobSearchDTO;
import cn.zc.student.domain.vo.JobDetailVO;
import cn.zc.student.domain.vo.JobListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职位Mapper接口（学生端）
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobMapper {

    /**
     * 查询职位列表（学生端）
     */
    List<JobListVO> selectJobListForStudent(@Param("dto") JobSearchDTO dto,
                                            @Param("studentId") Long studentId);

    /**
     * 查询职位详情（学生端）
     */
    JobDetailVO selectJobDetailForStudent(@Param("jobId") Long jobId,
                                          @Param("studentId") Long studentId);

    /**
     * 查询推荐职位列表
     */
    List<JobListVO> selectRecommendedJobs(@Param("studentId") Long studentId,
                                          @Param("limit") Integer limit);

    /**
     * 查询学生收藏的职位列表
     */
    List<JobListVO> selectFavoriteJobList(@Param("dto") JobSearchDTO dto,
                                          @Param("studentId") Long studentId);
}

