package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.JobAddDTO;
import cn.zc.enterprise.domain.dto.JobUpdateDTO;
import cn.zc.enterprise.domain.vo.JobDetailVO;
import cn.zc.enterprise.service.IJobService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 职位控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "职位管理", description = "职位相关接口")
@RestController
@RequestMapping("/enterprise/job")
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
    @Operation(summary = "职位列表查询", description = "查询企业发布的职位列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "jobName", required = false) String jobName,
                                  @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.list(pageNum, pageSize, status, jobName, enterpriseId);
    }

    /**
     * 职位详情查询
     */
    @Operation(summary = "职位详情查询", description = "查询指定职位的详细信息")
    @GetMapping("/detail")
    public R<JobDetailVO> detail(@RequestParam("jobId") Long jobId,
                                 @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.getDetail(jobId, enterpriseId);
    }

    /**
     * 发布职位
     */
    @Operation(summary = "发布职位", description = "发布新的招聘职位")
    @PostMapping("/add")
    public R<Long> add(@RequestBody JobAddDTO dto,
                      @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.add(dto, enterpriseId);
    }

    /**
     * 编辑职位
     */
    @Operation(summary = "编辑职位", description = "编辑已发布的职位信息")
    @PutMapping("/edit")
    public R<Void> edit(@RequestBody JobUpdateDTO dto,
                       @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.update(dto, enterpriseId);
    }

    /**
     * 删除职位
     */
    @Operation(summary = "删除职位", description = "删除指定的职位")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("jobId") Long jobId,
                         @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.delete(jobId, enterpriseId);
    }

    /**
     * 下线职位
     */
    @Operation(summary = "下线职位", description = "将职位下线，停止招聘")
    @PutMapping("/offline")
    public R<Void> offline(@RequestParam("jobId") Long jobId,
                          @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.offline(jobId, enterpriseId);
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

