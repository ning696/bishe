package cn.zc.admin.service;

import cn.zc.admin.domain.vo.JobStatisticsVO;
import cn.zc.admin.domain.vo.OverviewStatisticsVO;
import cn.zc.admin.domain.vo.UserStatisticsVO;
import cn.zc.common.core.domain.R;

/**
 * 数据统计服务接口
 * 
 * @author campus-hiring-system
 */
public interface IStatisticsService {

    /**
     * 用户统计
     */
    R<UserStatisticsVO> getUserStatistics(String startDate, String endDate);

    /**
     * 职位统计
     */
    R<JobStatisticsVO> getJobStatistics(String startDate, String endDate);

    /**
     * 运营数据统计
     */
    R<OverviewStatisticsVO> getOverviewStatistics();
}

