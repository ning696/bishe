package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.vo.CampusJobVO;
import cn.zc.enterprise.service.ICampusService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校园控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "校园管理", description = "校园相关接口")
@RestController
@RequestMapping("/enterprise/campus")
public class CampusController extends BaseController {

    @Autowired
    private ICampusService campusService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 校园列表查询
     */
    @Operation(summary = "校园列表查询", description = "查询可合作的校园列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam(value = "campusName", required = false) String campusName,
                                  @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return campusService.list(pageNum, pageSize, campusName);
    }

    /**
     * 职位校园关联查询
     */
    @Operation(summary = "职位校园关联查询", description = "查询指定职位关联的校园列表")
    @GetMapping("/job/list")
    public R<List<CampusJobVO>> getCampusJobList(@RequestParam("jobId") Long jobId,
                                                   @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return campusService.getCampusJobList(jobId);
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

