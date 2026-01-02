package cn.zc.admin.controller.statistics;

import cn.zc.admin.domain.vo.JobStatisticsVO;
import cn.zc.admin.domain.vo.OverviewStatisticsVO;
import cn.zc.admin.domain.vo.UserStatisticsVO;
import cn.zc.admin.service.IStatisticsService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据统计控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "数据统计（管理员）", description = "管理员端数据统计相关接口")
@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private IStatisticsService statisticsService;

    /**
     * 用户统计
     */
    @Operation(summary = "用户统计", description = "统计用户相关数据")
    @GetMapping("/user")
    public R<UserStatisticsVO> userStatistics(@RequestParam(required = false) String startDate,
                                               @RequestParam(required = false) String endDate) {
        return statisticsService.getUserStatistics(startDate, endDate);
    }

    /**
     * 职位统计
     */
    @Operation(summary = "职位统计", description = "统计职位相关数据")
    @GetMapping("/job")
    public R<JobStatisticsVO> jobStatistics(@RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate) {
        return statisticsService.getJobStatistics(startDate, endDate);
    }

    /**
     * 运营数据统计
     */
    @Operation(summary = "运营数据统计", description = "统计系统整体运营数据")
    @GetMapping("/overview")
    public R<OverviewStatisticsVO> overviewStatistics() {
        return statisticsService.getOverviewStatistics();
    }
}

