package cn.zc.system.controller;


import cn.zc.common.core.constants.HttpConstants;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.vo.LoginUserVO;
import cn.zc.system.domain.dto.LoginDTO;
import cn.zc.system.domain.dto.SysUserSaveDTO;
import cn.zc.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张策
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/sysUser")
@Tag(name = "管理员接口")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService iSysUserService;
    @GetMapping("/test")
    public String test() {
        return "测试成功";
    }
    @PostMapping("/login")
    @Operation(summary = "登录", description = "根据提供的用户名和密码登录⽤⼾")
    @ApiResponse(responseCode = "1000", description = "登录成功")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3102", description = "用户不存在")
    @ApiResponse(responseCode = "3103", description = "用户名或密码错误")
    public R<String> login(@RequestBody LoginDTO loginDTO) {
        return iSysUserService.login(loginDTO);
    }
    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "根据提供的token获取⽤⼾名")
    public R<LoginUserVO>  info(@RequestHeader(HttpConstants.AUTHENTICATION) String token){
        return iSysUserService.info(token);
    }
    @PostMapping("/add")
    @Operation(summary = "新增管理员", description = "根据提供的信息新增管理员⽤⼾")
    @ApiResponse(responseCode = "1000", description = "操作成功")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3101", description = "⽤⼾已存在")
    public R<Void> add(@RequestBody SysUserSaveDTO saveDTO) {
        return toR(iSysUserService.add(saveDTO));
    }
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除⽤⼾", description = "通过⽤⼾id删除⽤⼾")
    @Parameters(value = {
            @Parameter(name = "userId", in = ParameterIn.PATH, description = "⽤⼾ID")
                    })
            @ApiResponse(responseCode = "1000", description = "成功删除⽤⼾")
            @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
            @ApiResponse(responseCode = "3101", description = "⽤⼾不存在")
            public R<Void> delete(@PathVariable Long userId) {
        return null;
    }

    //修改我就不演⽰了和新增差不多

//    @Operation(summary = "⽤⼾详情", description = "根据查询条件查询⽤⼾详情")
//    @GetMapping("/detail")
// @Parameters(value = {
//             @Parameter(name = "userId", in = ParameterIn.QUERY, description =
//            "⽤⼾ID"),
//             @Parameter(name = "sex", in = ParameterIn.QUERY, description = "⽤⼾ 性别")
//             })
//             @ApiResponse(responseCode = "1000", description = "成功获取⽤⼾信息")
//             @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
//             @ApiResponse(responseCode = "3101", description = "⽤⼾不存在")
//             public R<SysUserVO> detail(Long userId, @RequestParam(required = false)
//            String sex) {
//         return null;
//         }
    @GetMapping("/list")
    public String list(String testId) {
        return "list"+testId;
    }
 }

