package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户统计VO
 * 
 * @author campus-hiring-system
 */
@Data
public class UserStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalUsers;
    private Long totalStudents;
    private Long totalEnterprises;
    private Long newUsersToday;
    private Long newUsersThisMonth;
    private Long activeUsers;
    private List<TrendData> trendData;

    @Data
    public static class TrendData implements Serializable {
        private String date;
        private Long studentCount;
        private Long enterpriseCount;
    }
}

