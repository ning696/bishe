package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;

import java.util.Map;

/**
 * 统计服务接口
 * 
 * @author campus-hiring-system
 */
public interface IStatisticsService {

    /**
     * 招聘效果统计
     */
    R<Map<String, Object>> recruitment(String startDate, String endDate, Long jobId, Long enterpriseId);

    /**
     * 数据分析
     */
    R<Map<String, Object>> analysis(String startDate, String endDate, Long enterpriseId);
}











