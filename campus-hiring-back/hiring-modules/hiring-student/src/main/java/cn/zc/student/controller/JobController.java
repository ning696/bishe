package cn.zc.student.controller;

import cn.zc.student.domain.dto.JobFavoriteDTO;
import cn.zc.student.domain.dto.JobSearchDTO;
import cn.zc.student.domain.vo.JobDetailVO;
import cn.zc.student.domain.vo.JobListVO;
import cn.zc.student.service.IJobService;
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
 * 职位控制器（学生端）
 * 
 * @author campus-hiring-system
 */
@Tag(name = "职位管理（学生端）", description = "学生端职位相关接口")
@RestController
@RequestMapping("/student/job")
public class JobController extends BaseController {

    @Autowired
    private IJobService jobService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 职位列表查询（支持搜索和筛选）
     */
    @Operation(summary = "职位列表查询", description = "查询职位列表，支持搜索和筛选")
    @PostMapping("/list")
    public R<TableDataInfo> list(@RequestBody JobSearchDTO dto,
                                 @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long studentId = getStudentId(authorization);
        return jobService.list(dto, studentId);
    }

    /**
     * 职位详情查询
     */
    @Operation(summary = "职位详情查询", description = "查询指定职位的详细信息")
    @GetMapping("/detail/{jobId}")
    public R<JobDetailVO> detail(@PathVariable("jobId") Long jobId,
                                  @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long studentId = getStudentId(authorization);
        return jobService.getDetail(jobId, studentId);
    }

    /**
     * 职位推荐
     */
    @Operation(summary = "职位推荐", description = "根据学生信息推荐匹配的职位")
    @GetMapping("/recommended")
    public R<List<JobListVO>> recommended(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.getRecommended(studentId);
    }

    /**
     * 收藏职位
     */
    @Operation(summary = "收藏职位", description = "收藏感兴趣的职位")
    @PostMapping("/favorite")
    public R<Void> favorite(@RequestBody JobFavoriteDTO dto,
                           @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.favorite(dto, studentId);
    }

    /**
     * 取消收藏
     */
    @Operation(summary = "取消收藏", description = "取消已收藏的职位")
    @DeleteMapping("/favorite/{jobId}")
    public R<Void> unfavorite(@PathVariable("jobId") Long jobId,
                              @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.unfavorite(jobId, studentId);
    }

    /**
     * 收藏列表查询
     */
    @Operation(summary = "收藏列表查询", description = "查询学生收藏的职位列表")
    @PostMapping("/favorite/list")
    public R<TableDataInfo> favoriteList(@RequestBody JobSearchDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return jobService.getFavoriteList(dto, studentId);
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

