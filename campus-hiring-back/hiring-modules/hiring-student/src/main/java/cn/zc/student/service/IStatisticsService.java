package cn.zc.student.service;

import cn.zc.common.core.domain.R;
import cn.zc.student.domain.vo.StatisticsOverviewVO;

/**
 * 统计服务接口
 * 
 * @author campus-hiring-system
 */
public interface IStatisticsService {

    /**
     * 获取统计概览
     */
    R<StatisticsOverviewVO> getOverview(Long studentId);
}


