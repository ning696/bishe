package cn.zc.admin.controller.job;

import cn.zc.admin.domain.dto.JobAuditDTO;
import cn.zc.admin.domain.vo.JobDetailVO;
import cn.zc.admin.service.IJobService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 职位审核控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "职位审核管理", description = "职位审核相关接口")
@RestController
@RequestMapping("/admin/job")
public class JobController extends BaseController {

    @Autowired
    private IJobService jobService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 职位列表查询
     */
    @Operation(summary = "职位列表查询", description = "查询所有职位列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer status,
                              @RequestParam(required = false) Long enterpriseId,
                              @RequestParam(required = false) String jobName,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return jobService.getJobList(status, enterpriseId, jobName, pageNum, pageSize);
    }

    /**
     * 职位详情查询
     */
    @Operation(summary = "职位详情查询", description = "查询指定职位的详细信息")
    @GetMapping("/detail")
    public R<JobDetailVO> detail(@RequestParam Long jobId) {
        return jobService.getJobDetail(jobId);
    }

    /**
     * 职位审核
     */
    @Operation(summary = "职位审核", description = "审核企业发布的职位")
    @PutMapping("/audit")
    public R<Void> audit(@RequestBody JobAuditDTO dto,
                        @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long adminId = tokenService.getUserId(claims);
        return jobService.auditJob(dto, adminId);
    }
}

