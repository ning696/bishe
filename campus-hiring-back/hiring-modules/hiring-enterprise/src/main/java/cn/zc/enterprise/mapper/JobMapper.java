package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.Job;
import cn.zc.enterprise.domain.vo.JobDetailVO;
import cn.zc.enterprise.domain.vo.JobListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职位Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 查询职位列表
     */
    List<JobListVO> selectJobList(@Param("enterpriseId") Long enterpriseId,
                                   @Param("status") Integer status,
                                   @Param("jobName") String jobName);

    /**
     * 查询职位详情
     */
    JobDetailVO selectJobDetail(@Param("jobId") Long jobId,
                                 @Param("enterpriseId") Long enterpriseId);
}

