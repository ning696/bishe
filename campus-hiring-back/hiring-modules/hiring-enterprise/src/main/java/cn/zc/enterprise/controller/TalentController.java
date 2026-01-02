package cn.zc.enterprise.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.vo.TalentRecommendVO;
import cn.zc.enterprise.service.ITalentService;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人才推荐控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "人才推荐", description = "人才推荐相关接口")
@RestController
@RequestMapping("/enterprise/talent")
public class TalentController extends BaseController {

    @Autowired
    private ITalentService talentService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 人才推荐
     */
    @Operation(summary = "人才推荐", description = "根据职位要求推荐匹配的人才")
    @GetMapping("/recommend")
    public R<TableDataInfo> recommend(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam(value = "jobId", required = false) Long jobId,
                                      @RequestParam(value = "requiredMajor", required = false) String requiredMajor,
                                      @RequestParam(value = "requiredSkills", required = false) String requiredSkills,
                                      @RequestParam(value = "requiredEducation", required = false) String requiredEducation,
                                      @RequestParam(value = "requiredExperience", required = false) Integer requiredExperience,
                                      @RequestParam(value = "campusId", required = false) Long campusId,
                                      @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return talentService.recommend(enterpriseId, pageNum, pageSize, jobId, requiredMajor, requiredSkills,
                requiredEducation, requiredExperience, campusId);
    }

    /**
     * 首页职位人才推荐
     */
    @Operation(summary = "职位人才推荐", description = "企业首页根据职位获取匹配人才")
    @GetMapping("/recommended")
    public R<List<TalentRecommendVO>> recommendedList(@RequestParam("jobId") Long jobId,
                                                      @RequestParam(value = "limit", required = false) Integer limit,
                                                      @RequestHeader("Authorization") String authorization) {
        Long enterpriseId = getEnterpriseId(authorization);
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return talentService.recommendForJob(enterpriseId, jobId, limit);
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






