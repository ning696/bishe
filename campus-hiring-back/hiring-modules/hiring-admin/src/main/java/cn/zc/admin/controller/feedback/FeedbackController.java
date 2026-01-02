package cn.zc.admin.controller.feedback;

import cn.zc.admin.domain.dto.FeedbackHandleDTO;
import cn.zc.admin.service.IFeedbackService;
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
 * 反馈管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "反馈管理（管理员）", description = "管理员端反馈相关接口")
@RestController
@RequestMapping("/admin/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 反馈列表查询
     */
    @Operation(summary = "反馈列表查询", description = "查询所有反馈列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer handleStatus,
                              @RequestParam(required = false) String feedbackType,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return feedbackService.getFeedbackList(handleStatus, feedbackType, pageNum, pageSize);
    }

    /**
     * 处理反馈
     */
    @Operation(summary = "处理反馈", description = "管理员处理反馈")
    @PutMapping("/handle")
    public R<Void> handle(@RequestBody FeedbackHandleDTO dto,
                         @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long adminId = tokenService.getUserId(claims);
        return feedbackService.handleFeedback(dto, adminId);
    }
}

