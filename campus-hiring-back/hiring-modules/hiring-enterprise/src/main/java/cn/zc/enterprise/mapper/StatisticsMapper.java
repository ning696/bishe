package cn.zc.enterprise.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 统计Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface StatisticsMapper {

    /**
     * 查询招聘效果统计
     */
    Map<String, Object> selectRecruitmentStatistics(@Param("enterpriseId") Long enterpriseId,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate,
                                                     @Param("jobId") Long jobId);

    /**
     * 查询职位统计数据
     */
    List<Map<String, Object>> selectJobStatistics(@Param("enterpriseId") Long enterpriseId,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate,
                                                   @Param("jobId") Long jobId);

    /**
     * 查询趋势数据
     */
    List<Map<String, Object>> selectTrendData(@Param("enterpriseId") Long enterpriseId,
                                               @Param("startDate") String startDate,
                                               @Param("endDate") String endDate,
                                               @Param("jobId") Long jobId);

    /**
     * 查询数据分析统计
     */
    Map<String, Object> selectAnalysisStatistics(@Param("enterpriseId") Long enterpriseId,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate);

    /**
     * 查询表现最好的职位
     */
    List<Map<String, Object>> selectTopPerformingJobs(@Param("enterpriseId") Long enterpriseId,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);
}











