package cn.zc.student.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.student.domain.vo.StatisticsOverviewVO;
import cn.zc.student.mapper.StatisticsMapper;
import cn.zc.student.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public R<StatisticsOverviewVO> getOverview(Long studentId) {
        StatisticsOverviewVO overview = statisticsMapper.selectStatisticsOverview(studentId);
        if (overview == null) {
            // 如果查询结果为空，返回默认值
            overview = new StatisticsOverviewVO();
            overview.setTotalApplications(0);
            overview.setPendingApplications(0);
            overview.setPassedApplications(0);
            overview.setTotalInterviews(0);
            overview.setScheduledInterviews(0);
            overview.setCompletedInterviews(0);
            overview.setTotalFavorites(0);
            overview.setTotalResumes(0);
            overview.setDefaultResumeId(null);
            overview.setResumeCompleteness(0);
            overview.setHeadImage(null);
        }
        return R.ok(overview);
    }
}


