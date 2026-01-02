package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.service.TokenService;
import cn.zc.enterprise.domain.dto.FeedbackAddDTO;
import cn.zc.enterprise.service.IFeedbackService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 反馈控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "反馈管理（企业端）", description = "企业端反馈相关接口")
@RestController
@RequestMapping("/enterprise/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 提交反馈
     */
    @Operation(summary = "提交反馈", description = "企业提交反馈信息")
    @PostMapping("/add")
    public R<Void> add(@RequestBody FeedbackAddDTO dto,
                       @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return feedbackService.add(dto, enterpriseId);
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

