package cn.zc.student.controller;

import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.vo.StudentDetailVO;
import cn.zc.student.domain.vo.StudentInfoVO;
import cn.zc.student.service.IStudentService;
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
 * 学生控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "学生管理", description = "学生相关接口")
@RestController
@RequestMapping("/student/student")
public class StudentController extends BaseController {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 学生注册
     */
    @Operation(summary = "学生注册", description = "学生用户注册接口")
    @PostMapping("/register")
    public R<Void> register(@RequestBody StudentRegisterDTO dto) {
        return studentService.register(dto);
    }

    /**
     * 发送验证码
     */
    @Operation(summary = "发送验证码", description = "发送验证码接口")
    @PostMapping("/sendCode")
    public R<Void> sendCode(@RequestBody SendCodeDTO dto) {
        return studentService.sendCode(dto);
    }

    /**
     * 学生登录
     */
    @Operation(summary = "学生登录", description = "学生用户登录接口")
    @PostMapping("/login")
    public R<String> login(@RequestBody StudentLoginDTO dto) {
        return studentService.login(dto);
    }

    /**
     * 获取学生信息
     */
    @Operation(summary = "获取学生信息", description = "获取当前登录学生的基本信息")
    @GetMapping("/info")
    public R<StudentInfoVO> info(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.getInfo(studentId);
    }

    /**
     * 获取学生信息（服务间调用）
     */
    @Operation(summary = "获取学生信息（服务间调用）", description = "根据学生ID获取学生基本信息，供服务间调用")
    @GetMapping("/info/{studentId}")
    public R<cn.zc.api.domain.vo.StudentInfoVO> getStudentInfoById(@PathVariable("studentId") Long studentId) {
        return studentService.getInfoForService(studentId);
    }

    /**
     * 学生详情查询
     */
    @Operation(summary = "学生详情查询", description = "查询当前登录学生的详细信息")
    @GetMapping("/detail")
    public R<StudentDetailVO> detail(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.getDetail(studentId);
    }

    /**
     * 更新学生信息
     */
    @Operation(summary = "更新学生信息", description = "更新当前登录学生的信息")
    @PutMapping("/edit")
    public R<Void> edit(@RequestBody StudentUpdateDTO dto,
                       @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.updateInfo(dto, studentId);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "修改学生用户密码")
    @PutMapping("/changePassword")
    public R<Void> changePassword(@RequestBody PasswordChangeDTO dto,
                                 @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.changePassword(dto, studentId);
    }

    /**
     * 更新头像
     */
    @Operation(summary = "更新头像", description = "更新学生头像图片")
    @PutMapping("/headImage/update")
    public R<Void> updateHeadImage(@RequestBody HeadImageUpdateDTO dto,
                                  @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.updateHeadImage(dto, studentId);
    }

    /**
     * 学生文件上传
     */
    @Operation(summary = "学生文件上传", description = "上传学生相关文件，返回文件访问地址")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<OSSResult> upload(@RequestParam("file") MultipartFile file,
                               @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = tokenService.getClaims(token, secret);
        if (claims == null) {
            return R.fail(cn.zc.common.core.enums.ResultCode.FAILED_UNAUTHORIZED);
        }
        Long studentId = tokenService.getUserId(claims);
        return studentService.uploadFile(file, studentId);
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "学生用户退出登录")
    @DeleteMapping("/logout")
    public R<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return studentService.logout(token);
    }
}

