package cn.zc.enterprise.controller;

import cn.zc.api.domain.vo.ResumeDetailVO;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.ResumeApplicationStatusUpdateDTO;
import cn.zc.enterprise.service.IResumeService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 简历管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "简历管理", description = "简历相关接口")
@RestController
@RequestMapping("/enterprise/resume")
public class ResumeController extends BaseController {

    @Autowired
    private IResumeService resumeService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 简历列表查询
     */
    @Operation(summary = "简历列表查询", description = "查询投递到企业的简历列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam(value = "jobId", required = false) Long jobId,
                                 @RequestParam(value = "applicationStatus", required = false) Integer applicationStatus,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.list(pageNum, pageSize, jobId, applicationStatus, keyword, enterpriseId);
    }

    /**
     * 简历详情查询
     */
    @Operation(summary = "简历详情查询", description = "查询指定简历的详细信息")
    @GetMapping("/detail")
    public R<ResumeDetailVO> detail(@RequestParam("resumeId") Long resumeId,
                                    @RequestParam(value = "applicationId", required = false) Long applicationId,
                                    @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.detail(resumeId, applicationId);
    }

    /**
     * 更新简历申请状态
     */
    @Operation(summary = "更新简历申请状态", description = "更新简历的申请状态（如：已通过、已拒绝等）")
    @PutMapping("/updateStatus")
    public R<Void> updateStatus(@RequestBody ResumeApplicationStatusUpdateDTO dto,
                                @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.updateStatus(dto);
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






