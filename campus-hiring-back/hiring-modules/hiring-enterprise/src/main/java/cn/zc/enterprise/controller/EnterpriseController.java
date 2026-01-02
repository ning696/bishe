package cn.zc.enterprise.controller;

import cn.zc.enterprise.domain.dto.*;
import cn.zc.enterprise.domain.vo.EnterpriseDetailVO;
import cn.zc.enterprise.domain.vo.EnterpriseInfoVO;
import cn.zc.enterprise.service.IEnterpriseService;
import cn.zc.minio.domain.OSSResult;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 企业控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "企业管理", description = "企业相关接口")
@RestController
@RequestMapping("/enterprise/enterprise")
public class EnterpriseController extends BaseController {

    @Autowired
    private IEnterpriseService enterpriseService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 企业注册
     */
    @Operation(summary = "企业注册", description = "企业用户注册接口")
    @PostMapping("/register")
    public R<Void> register(@RequestBody EnterpriseRegisterDTO dto) {
        return enterpriseService.register(dto);
    }

    /**
     * 发送验证码
     */
    @Operation(summary = "发送验证码", description = "发送验证码接口")
    @PostMapping("/sendCode")
    public R<Void> sendCode(@RequestBody SendCodeDTO dto) {
        return enterpriseService.sendCode(dto);
    }

    /**
     * 企业登录
     */
    @Operation(summary = "企业登录", description = "企业用户登录接口")
    @PostMapping("/login")
    public R<String> login(@RequestBody EnterpriseLoginDTO dto) {
        return enterpriseService.login(dto);
    }

    /**
     * 获取企业信息
     */
    @Operation(summary = "获取企业信息", description = "获取当前登录企业的基本信息")
    @GetMapping("/info")
    public R<EnterpriseInfoVO> info(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.getInfo(enterpriseId);
    }

    /**
     * 企业详情查询
     */
    @Operation(summary = "企业详情查询", description = "查询当前登录企业的详细信息")
    @GetMapping("/detail")
    public R<EnterpriseDetailVO> detail(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.getDetail(enterpriseId);
    }

    /**
     * 更新企业信息
     */
    @Operation(summary = "更新企业信息", description = "更新当前登录企业的信息")
    @PutMapping("/edit")
    public R<Void> edit(@RequestBody EnterpriseUpdateDTO dto,
                       @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.updateInfo(dto, enterpriseId);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "修改企业用户密码")
    @PutMapping("/changePassword")
    public R<Void> changePassword(@RequestBody PasswordChangeDTO dto,
                                 @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.changePassword(dto, enterpriseId);
    }

    /**
     * 更新企业Logo
     */
    @Operation(summary = "更新企业Logo", description = "更新企业Logo图片")
    @PutMapping("/logo/update")
    public R<Void> updateLogo(@RequestBody LogoUpdateDTO dto,
                             @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.updateLogo(dto, enterpriseId);
    }

    /**
     * 企业文件上传
     */
    @Operation(summary = "企业文件上传", description = "上传企业相关文件，返回文件访问地址")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<OSSResult> upload(@RequestParam("file") MultipartFile file,
                               @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.uploadFile(file, enterpriseId);
    }

    /**
     * 企业认证申请
     */
    @Operation(summary = "企业认证申请", description = "提交企业认证申请")
    @PostMapping(value = "/certification/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> applyCertification(@RequestParam("file") MultipartFile file,
                                     @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long enterpriseId = tokenService.getUserId(claims);
        return enterpriseService.applyCertification(file, enterpriseId);
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "企业用户退出登录")
    @DeleteMapping("/logout")
    public R<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return enterpriseService.logout(token);
    }
}

