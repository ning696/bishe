package cn.zc.student.controller;

import cn.zc.student.domain.dto.InterviewApplyDTO;
import cn.zc.student.domain.vo.InterviewDetailVO;
import cn.zc.student.domain.vo.InterviewEvaluationVO;
import cn.zc.student.domain.vo.InterviewListVO;
import cn.zc.student.service.IInterviewService;
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

import java.util.List;

/**
 * 面试控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "面试管理（学生端）", description = "学生端面试相关接口")
@RestController
@RequestMapping("/student/interview")
public class InterviewController extends BaseController {

    @Autowired
    private IInterviewService interviewService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 面试申请
     */
    @Operation(summary = "面试申请", description = "学生提交面试申请")
    @PostMapping("/apply")
    public R<Void> apply(@RequestBody InterviewApplyDTO dto,
                       @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.apply(dto, studentId);
    }

    /**
     * 申请记录查询
     */
    @Operation(summary = "申请记录查询", description = "查询学生的面试申请记录")
    @GetMapping("/application/list")
    public R<List<InterviewListVO>> applicationList(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.getApplicationList(studentId);
    }

    /**
     * 申请记录分页查询
     */
    @Operation(summary = "申请记录分页查询", description = "分页查询学生的面试申请记录")
    @GetMapping("/application/page")
    public R<TableDataInfo> applicationPage(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam(value = "status", required = false) Integer status,
                                            @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.pageApplication(studentId, pageNum, pageSize, status);
    }

    /**
     * 面试安排查询
     */
    @Operation(summary = "面试安排查询", description = "查询学生的面试安排列表")
    @GetMapping("/list")
    public R<List<InterviewListVO>> interviewList(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.getInterviewList(studentId);
    }

    /**
     * 面试详情查询
     */
    @Operation(summary = "面试详情查询", description = "查询指定面试的详细信息")
    @GetMapping("/detail/{interviewId}")
    public R<InterviewDetailVO> detail(@PathVariable("interviewId") Long interviewId,
                                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.getDetail(interviewId, studentId);
    }

    /**
     * 面试评价查看
     */
    @Operation(summary = "面试评价查看", description = "查看面试评价结果")
    @GetMapping("/evaluation/{interviewId}")
    public R<InterviewEvaluationVO> evaluation(@PathVariable("interviewId") Long interviewId,
                                               @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return interviewService.getEvaluation(interviewId, studentId);
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

