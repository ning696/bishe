package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 职位统计VO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalJobs;
    private Long publishedJobs;
    private Long pendingJobs;
    private Long rejectedJobs;
    private Long totalApplications;
    private Double averageApplicationsPerJob;
    private List<TrendData> trendData;

    @Data
    public static class TrendData implements Serializable {
        private String date;
        private Long jobCount;
        private Long applicationCount;
    }
}

