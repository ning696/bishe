package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.JobApplication;
import cn.zc.enterprise.domain.vo.JobApplicationListVO;
import cn.zc.enterprise.domain.vo.ResumeApplicationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职位申请Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobApplicationMapper extends BaseMapper<JobApplication> {

    /**
     * 查询面试申请列表
     */
    List<JobApplicationListVO> selectJobApplicationList(@Param("enterpriseId") Long enterpriseId,
                                                         @Param("jobId") Long jobId,
                                                         @Param("applicationStatus") Integer applicationStatus);

    /**
     * 查询简历申请列表（用于企业端简历管理）
     */
    List<ResumeApplicationListVO> selectResumeApplicationList(@Param("enterpriseId") Long enterpriseId,
                                                             @Param("jobId") Long jobId,
                                                             @Param("applicationStatus") Integer applicationStatus,
                                                             @Param("keyword") String keyword);
}











