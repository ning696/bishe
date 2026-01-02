package cn.zc.student.mapper;

import cn.zc.student.domain.vo.StatisticsOverviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 统计Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface StatisticsMapper {

    /**
     * 查询学生统计概览
     */
    StatisticsOverviewVO selectStatisticsOverview(@Param("studentId") Long studentId);
}


