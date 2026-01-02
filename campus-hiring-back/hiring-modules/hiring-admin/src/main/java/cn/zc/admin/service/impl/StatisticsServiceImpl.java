package cn.zc.admin.service.impl;

import cn.zc.admin.domain.entity.Enterprise;
import cn.zc.admin.domain.entity.Job;
import cn.zc.admin.domain.entity.Student;
import cn.zc.admin.domain.vo.JobStatisticsVO;
import cn.zc.admin.domain.vo.OverviewStatisticsVO;
import cn.zc.admin.domain.vo.UserStatisticsVO;
import cn.zc.admin.mapper.EnterpriseMapper;
import cn.zc.admin.mapper.JobMapper;
import cn.zc.admin.mapper.StudentMapper;
import cn.zc.admin.service.IStatisticsService;
import cn.zc.common.core.domain.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据统计服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private JobMapper jobMapper;

    @Override
    public R<UserStatisticsVO> getUserStatistics(String startDate, String endDate) {
        UserStatisticsVO vo = new UserStatisticsVO();

        // 总用户数
        long totalStudents = studentMapper.selectCount(null);
        long totalEnterprises = enterpriseMapper.selectCount(null);
        vo.setTotalUsers(totalStudents + totalEnterprises);
        vo.setTotalStudents(totalStudents);
        vo.setTotalEnterprises(totalEnterprises);

        // 今日新增用户
        LambdaQueryWrapper<Student> studentTodayWrapper = new LambdaQueryWrapper<>();
        studentTodayWrapper.ge(Student::getCreateTime, LocalDate.now().atStartOfDay());
        long newStudentsToday = studentMapper.selectCount(studentTodayWrapper);

        LambdaQueryWrapper<Enterprise> enterpriseTodayWrapper = new LambdaQueryWrapper<>();
        enterpriseTodayWrapper.ge(Enterprise::getCreateTime, LocalDate.now().atStartOfDay());
        long newEnterprisesToday = enterpriseMapper.selectCount(enterpriseTodayWrapper);
        vo.setNewUsersToday(newStudentsToday + newEnterprisesToday);

        // 本月新增用户
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LambdaQueryWrapper<Student> studentMonthWrapper = new LambdaQueryWrapper<>();
        studentMonthWrapper.ge(Student::getCreateTime, firstDayOfMonth.atStartOfDay());
        long newStudentsMonth = studentMapper.selectCount(studentMonthWrapper);

        LambdaQueryWrapper<Enterprise> enterpriseMonthWrapper = new LambdaQueryWrapper<>();
        enterpriseMonthWrapper.ge(Enterprise::getCreateTime, firstDayOfMonth.atStartOfDay());
        long newEnterprisesMonth = enterpriseMapper.selectCount(enterpriseMonthWrapper);
        vo.setNewUsersThisMonth(newStudentsMonth + newEnterprisesMonth);

        // 活跃用户（最近30天有活动的用户，这里简化处理，使用总用户数）
        vo.setActiveUsers(totalStudents + totalEnterprises);

        // 趋势数据（简化处理，返回空列表）
        vo.setTrendData(new ArrayList<>());

        return R.ok(vo);
    }

    @Override
    public R<JobStatisticsVO> getJobStatistics(String startDate, String endDate) {
        JobStatisticsVO vo = new JobStatisticsVO();

        // 总职位数
        long totalJobs = jobMapper.selectCount(null);
        vo.setTotalJobs(totalJobs);

        // 已发布职位数
        LambdaQueryWrapper<Job> publishedWrapper = new LambdaQueryWrapper<>();
        publishedWrapper.eq(Job::getStatus, 1);
        long publishedJobs = jobMapper.selectCount(publishedWrapper);
        vo.setPublishedJobs(publishedJobs);

        // 待审核职位数
        LambdaQueryWrapper<Job> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Job::getStatus, 0);
        long pendingJobs = jobMapper.selectCount(pendingWrapper);
        vo.setPendingJobs(pendingJobs);

        // 已拒绝职位数
        LambdaQueryWrapper<Job> rejectedWrapper = new LambdaQueryWrapper<>();
        rejectedWrapper.eq(Job::getStatus, 2);
        long rejectedJobs = jobMapper.selectCount(rejectedWrapper);
        vo.setRejectedJobs(rejectedJobs);

        // 总申请数（需要查询 job_application 表，这里简化处理）
        vo.setTotalApplications(0L);
        vo.setAverageApplicationsPerJob(0.0);

        // 趋势数据（简化处理，返回空列表）
        vo.setTrendData(new ArrayList<>());

        return R.ok(vo);
    }

    @Override
    public R<OverviewStatisticsVO> getOverviewStatistics() {
        OverviewStatisticsVO vo = new OverviewStatisticsVO();

        // 总用户数
        long totalStudents = studentMapper.selectCount(null);
        long totalEnterprises = enterpriseMapper.selectCount(null);
        vo.setTotalUsers(totalStudents + totalEnterprises);

        // 总职位数
        long totalJobs = jobMapper.selectCount(null);
        vo.setTotalJobs(totalJobs);

        // 总申请数（简化处理）
        vo.setTotalApplications(0L);
        vo.setTotalInterviews(0L);
        vo.setTotalConsultations(0L);
        vo.setTotalComplaints(0L);

        // 今日数据
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Student> studentTodayWrapper = new LambdaQueryWrapper<>();
        studentTodayWrapper.ge(Student::getCreateTime, today.atStartOfDay());
        long newStudentsToday = studentMapper.selectCount(studentTodayWrapper);

        LambdaQueryWrapper<Enterprise> enterpriseTodayWrapper = new LambdaQueryWrapper<>();
        enterpriseTodayWrapper.ge(Enterprise::getCreateTime, today.atStartOfDay());
        long newEnterprisesToday = enterpriseMapper.selectCount(enterpriseTodayWrapper);
        vo.setTodayNewUsers(newStudentsToday + newEnterprisesToday);

        LambdaQueryWrapper<Job> jobTodayWrapper = new LambdaQueryWrapper<>();
        jobTodayWrapper.ge(Job::getCreateTime, today.atStartOfDay());
        vo.setTodayNewJobs(jobMapper.selectCount(jobTodayWrapper));

        vo.setTodayActiveUsers(totalStudents + totalEnterprises);
        vo.setTodayNewApplications(0L);

        return R.ok(vo);
    }
}

