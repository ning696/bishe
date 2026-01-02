package cn.zc.student.controller;

import cn.zc.student.domain.dto.ResumeAddDTO;
import cn.zc.student.domain.dto.ResumeDeliveryDTO;
import cn.zc.student.domain.dto.ResumeUpdateDTO;
import cn.zc.student.domain.vo.ResumeDetailVO;
import cn.zc.student.domain.vo.ResumeListVO;
import cn.zc.student.service.IResumeService;
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
 * 简历控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "简历管理（学生端）", description = "学生端简历相关接口")
@RestController
@RequestMapping("/student/resume")
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
    @Operation(summary = "简历列表查询", description = "查询当前学生的简历列表")
    @GetMapping("/list")
    public R<List<ResumeListVO>> list(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.list(studentId);
    }

    /**
     * 简历详情查询
     */
    @Operation(summary = "简历详情查询", description = "查询指定简历的详细信息")
    @GetMapping("/detail/{resumeId}")
    public R<ResumeDetailVO> detail(@PathVariable("resumeId") Long resumeId,
                                    @RequestHeader(value = "Authorization", required = false) String authorization) {
        // 如果有 Authorization，验证学生身份（学生端调用）
        if (authorization != null && !authorization.isEmpty()) {
            Long studentId = getStudentId(authorization);
            if (studentId == null) {
                return R.fail(ResultCode.FAILED_UNAUTHORIZED);
            }
            return resumeService.getDetail(resumeId, studentId);
        } else {
            // 服务间调用，不需要验证学生身份，直接返回简历详情
            return resumeService.getDetailWithoutAuth(resumeId);
        }
    }

    /**
     * 创建简历
     */
    @Operation(summary = "创建简历", description = "创建新的简历")
    @PostMapping("/add")
    public R<Void> add(@RequestBody ResumeAddDTO dto,
                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.add(dto, studentId);
    }

    /**
     * 更新简历
     */
    @Operation(summary = "更新简历", description = "更新已存在的简历信息")
    @PutMapping("/update")
    public R<Void> update(@RequestBody ResumeUpdateDTO dto,
                        @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.update(dto, studentId);
    }

    /**
     * 删除简历
     */
    @Operation(summary = "删除简历", description = "删除指定的简历")
    @DeleteMapping("/delete/{resumeId}")
    public R<Void> delete(@PathVariable("resumeId") Long resumeId,
                        @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.delete(resumeId, studentId);
    }

    /**
     * 投递简历
     */
    @Operation(summary = "投递简历", description = "向企业投递简历")
    @PostMapping("/delivery")
    public R<Void> delivery(@RequestBody ResumeDeliveryDTO dto,
                           @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return resumeService.delivery(dto, studentId);
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

