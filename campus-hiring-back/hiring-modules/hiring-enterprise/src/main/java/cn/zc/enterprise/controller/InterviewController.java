package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.InterviewApplicationHandleDTO;
import cn.zc.enterprise.domain.dto.InterviewArrangeDTO;
import cn.zc.enterprise.domain.dto.InterviewEvaluationDTO;
import cn.zc.enterprise.service.IInterviewService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 面试管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "面试管理", description = "面试相关接口")
@RestController
@RequestMapping("/enterprise/interview")
public class InterviewController extends BaseController {

    @Autowired
    private IInterviewService interviewService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 面试申请查看
     */
    @Operation(summary = "面试申请查看", description = "查看学生提交的面试申请列表")
    @GetMapping("/application/list")
    public R<TableDataInfo> applicationList(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam(value = "jobId", required = false) Long jobId,
                                            @RequestParam(value = "applicationStatus", required = false) Integer applicationStatus,
                                            @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.applicationList(pageNum, pageSize, jobId, applicationStatus, enterpriseId);
    }

    /**
     * 处理面试申请
     */
    @Operation(summary = "处理面试申请", description = "处理学生的面试申请（通过或拒绝）")
    @PutMapping("/application/handle")
    public R<Void> handleApplication(@RequestBody InterviewApplicationHandleDTO dto,
                                    @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.handleApplication(dto);
    }

    /**
     * 安排面试
     */
    @Operation(summary = "安排面试", description = "为企业安排面试时间和地点")
    @PostMapping("/arrange")
    public R<Long> arrange(@RequestBody InterviewArrangeDTO dto,
                          @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.arrange(dto, enterpriseId);
    }

    /**
     * 面试列表查询
     */
    @Operation(summary = "面试列表查询", description = "查询企业的面试安排列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "jobId", required = false) Long jobId,
                                @RequestParam(value = "interviewStatus", required = false) Integer interviewStatus,
                                @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.list(pageNum, pageSize, jobId, interviewStatus, enterpriseId);
    }

    /**
     * 面试评价
     */
    @Operation(summary = "面试评价", description = "对面试结果进行评价和记录")
    @PostMapping("/evaluation")
    public R<Long> evaluation(@RequestBody InterviewEvaluationDTO dto,
                             @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.evaluation(dto, enterpriseId);
    }

    /**
     * 从Token中获取企业ID
     */
    private Long getEnterpriseId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authorization.replace("Bearer ", "");
            Claims claims = tokenService.getClaims(token, secret);
            if (claims == null) {
                return null;
            }
            return tokenService.getUserId(claims);
        } catch (Exception e) {
            return null;
        }
    }
}






