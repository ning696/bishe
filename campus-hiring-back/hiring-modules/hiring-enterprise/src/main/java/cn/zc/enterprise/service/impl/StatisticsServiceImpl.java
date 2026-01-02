package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.enterprise.mapper.StatisticsMapper;
import cn.zc.enterprise.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public R<Map<String, Object>> recruitment(String startDate, String endDate, Long jobId, Long enterpriseId) {
        // 查询总体统计
        Map<String, Object> statistics = statisticsMapper.selectRecruitmentStatistics(
                enterpriseId, startDate, endDate, jobId);
        
        // 查询职位统计数据
        List<Map<String, Object>> jobStatistics = statisticsMapper.selectJobStatistics(
                enterpriseId, startDate, endDate, jobId);
        
        // 查询趋势数据
        List<Map<String, Object>> trendData = statisticsMapper.selectTrendData(
                enterpriseId, startDate, endDate, jobId);
        
        Map<String, Object> result = new HashMap<>();
        result.putAll(statistics);
        result.put("jobStatistics", jobStatistics);
        result.put("trendData", trendData);
        
        return R.ok(result);
    }

    @Override
    public R<Map<String, Object>> analysis(String startDate, String endDate, Long enterpriseId) {
        // 查询分析统计
        Map<String, Object> statistics = statisticsMapper.selectAnalysisStatistics(
                enterpriseId, startDate, endDate);
        
        // 查询表现最好的职位
        List<Map<String, Object>> topPerformingJobs = statisticsMapper.selectTopPerformingJobs(
                enterpriseId, startDate, endDate);
        
        // 生成建议
        List<String> recommendations = generateRecommendations(statistics);
        
        Map<String, Object> result = new HashMap<>();
        result.putAll(statistics);
        result.put("topPerformingJobs", topPerformingJobs);
        result.put("recommendations", recommendations);
        
        return R.ok(result);
    }

    /**
     * 生成建议
     */
    private List<String> generateRecommendations(Map<String, Object> statistics) {
        List<String> recommendations = new ArrayList<>();
        
        Object viewToApplicationRateObj = statistics.get("viewToApplicationRate");
        if (viewToApplicationRateObj != null) {
            double viewToApplicationRate = Double.parseDouble(viewToApplicationRateObj.toString());
            if (viewToApplicationRate < 10) {
                recommendations.add("建议优化职位描述，提高吸引力");
            }
        }
        
        Object applicationToInterviewRateObj = statistics.get("applicationToInterviewRate");
        if (applicationToInterviewRateObj != null) {
            double applicationToInterviewRate = Double.parseDouble(applicationToInterviewRateObj.toString());
            if (applicationToInterviewRate < 20) {
                recommendations.add("建议提高简历筛选效率，增加面试机会");
            }
        }
        
        Object interviewToHireRateObj = statistics.get("interviewToHireRate");
        if (interviewToHireRateObj != null) {
            double interviewToHireRate = Double.parseDouble(interviewToHireRateObj.toString());
            if (interviewToHireRate < 15) {
                recommendations.add("建议优化面试流程，提高录用率");
            }
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("招聘效果良好，继续保持");
        }
        
        return recommendations;
    }
}











