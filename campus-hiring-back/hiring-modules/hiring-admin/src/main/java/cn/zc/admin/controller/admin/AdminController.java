package cn.zc.admin.controller.admin;

import cn.zc.admin.domain.dto.AdminLoginDTO;
import cn.zc.admin.domain.vo.AdminInfoVO;
import cn.zc.admin.service.IAdminService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "管理员管理", description = "管理员相关接口")
@RestController
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 管理员登录
     */
    @Operation(summary = "管理员登录", description = "管理员用户登录接口")
    @PostMapping("/login")
    public R<String> login(@RequestBody AdminLoginDTO loginDTO) {
        return adminService.login(loginDTO);
    }

    /**
     * 获取管理员信息
     */
    @Operation(summary = "获取管理员信息", description = "获取当前登录管理员的基本信息")
    @GetMapping("/info")
    public R<AdminInfoVO> info(@RequestHeader("Authorization") String authorization) {
        // 解析Token获取管理员ID
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long adminId = tokenService.getUserId(claims);
        return adminService.getInfo(adminId);
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "管理员用户退出登录")
    @DeleteMapping("/logout")
    public R<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return adminService.logout(token);
    }
}

