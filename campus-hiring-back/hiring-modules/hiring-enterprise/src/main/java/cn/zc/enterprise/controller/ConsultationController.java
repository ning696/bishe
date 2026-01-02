package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.ConsultationReplyDTO;
import cn.zc.enterprise.service.IConsultationService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 线上咨询控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "线上咨询", description = "线上咨询相关接口")
@RestController
@RequestMapping("/enterprise/consultation")
public class ConsultationController extends BaseController {

    @Autowired
    private IConsultationService consultationService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 咨询列表查询
     */
    @Operation(summary = "咨询列表查询", description = "查询学生向企业提交的咨询列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam(value = "status", required = false) Integer status,
                                 @RequestParam(value = "studentId", required = false) Long studentId,
                                 @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return consultationService.list(pageNum, pageSize, status, studentId, enterpriseId);
    }

    /**
     * 回复咨询
     */
    @Operation(summary = "回复咨询", description = "企业回复学生的咨询问题")
    @PutMapping("/reply")
    public R<Void> reply(@RequestBody ConsultationReplyDTO dto,
                        @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return consultationService.reply(dto, enterpriseId);
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






