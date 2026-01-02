package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 运营数据统计VO
 * 
 * @author campus-hiring-system
 */
@Data
public class OverviewStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalUsers;
    private Long totalJobs;
    private Long totalApplications;
    private Long totalInterviews;
    private Long totalConsultations;
    private Long totalComplaints;
    private Long todayActiveUsers;
    private Long todayNewUsers;
    private Long todayNewJobs;
    private Long todayNewApplications;
}

