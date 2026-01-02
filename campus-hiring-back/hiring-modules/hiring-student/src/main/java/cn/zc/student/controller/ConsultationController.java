package cn.zc.student.controller;

import cn.zc.student.domain.dto.ConsultationAddDTO;
import cn.zc.student.domain.vo.ConsultationListVO;
import cn.zc.student.service.IConsultationService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 咨询控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "咨询管理（学生端）", description = "学生端咨询相关接口")
@RestController
@RequestMapping("/student/consultation")
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
    public R<List<ConsultationListVO>> list(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return consultationService.list(studentId);
    }

    /**
     * 发起咨询
     */
    @Operation(summary = "发起咨询", description = "学生向企业发起咨询")
    @PostMapping("/add")
    public R<Void> add(@RequestBody ConsultationAddDTO dto,
                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return consultationService.add(dto, studentId);
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

