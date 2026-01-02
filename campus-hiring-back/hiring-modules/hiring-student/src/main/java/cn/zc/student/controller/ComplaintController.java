package cn.zc.student.controller;

import cn.zc.student.domain.dto.ComplaintAddDTO;
import cn.zc.student.domain.dto.ComplaintQueryDTO;
import cn.zc.student.service.IComplaintService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 投诉控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "投诉管理（学生端）", description = "学生端投诉相关接口")
@RestController
@RequestMapping("/student/complaint")
public class ComplaintController extends BaseController {

    @Autowired
    private IComplaintService complaintService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 提交投诉
     */
    @Operation(summary = "提交投诉", description = "学生提交投诉信息")
    @PostMapping("/add")
    public R<Long> add(@RequestBody ComplaintAddDTO dto,
                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return complaintService.add(dto, studentId);
    }

    /**
     * 投诉查询
     */
    @Operation(summary = "投诉查询", description = "查询学生提交的投诉列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(ComplaintQueryDTO query,
                                 @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return complaintService.list(query, studentId);
    }

    /**
     * 从Token中获取学生ID
     */
    private Long getStudentId(String authorization) {
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

