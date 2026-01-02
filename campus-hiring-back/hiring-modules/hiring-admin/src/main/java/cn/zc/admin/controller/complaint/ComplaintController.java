package cn.zc.admin.controller.complaint;

import cn.zc.admin.domain.dto.ComplaintHandleDTO;
import cn.zc.admin.domain.vo.ComplaintDetailVO;
import cn.zc.admin.service.IComplaintService;
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
 * 投诉管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "投诉管理（管理员）", description = "管理员端投诉相关接口")
@RestController
@RequestMapping("/admin/complaint")
public class ComplaintController extends BaseController {

    @Autowired
    private IComplaintService complaintService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 投诉列表查询
     */
    @Operation(summary = "投诉列表查询", description = "查询所有投诉列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer handleStatus,
                              @RequestParam(required = false) Integer complaintType,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return complaintService.getComplaintList(handleStatus, complaintType, pageNum, pageSize);
    }

    /**
     * 投诉详情查询
     */
    @Operation(summary = "投诉详情查询", description = "查询指定投诉的详细信息")
    @GetMapping("/detail")
    public R<ComplaintDetailVO> detail(@RequestParam Long complaintId) {
        return complaintService.getComplaintDetail(complaintId);
    }

    /**
     * 处理投诉
     */
    @Operation(summary = "处理投诉", description = "管理员处理投诉")
    @PutMapping("/handle")
    public R<Void> handle(@RequestBody ComplaintHandleDTO dto,
                         @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long adminId = tokenService.getUserId(claims);
        return complaintService.handleComplaint(dto, adminId);
    }
}

