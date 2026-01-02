package cn.zc.student.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.service.TokenService;
import cn.zc.student.service.IStudentPersonalApplicationService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Tag(name = "个人中心-职位申请（学生端）", description = "学生端个人中心职位申请列表")
@RestController
@RequestMapping("/student/personal/application")
public class PersonalApplicationController extends BaseController {

	@Autowired
	private IStudentPersonalApplicationService personalApplicationService;

	@Autowired
	private TokenService tokenService;

	@Value("${jwt.secret}")
	private String secret;

	@Operation(summary = "职位申请分页列表", description = "查询学生的职位申请记录（job_application）")
	@GetMapping("/list")
	public R<TableDataInfo> list(@RequestParam("pageNum") Integer pageNum,
	                             @RequestParam("pageSize") Integer pageSize,
	                             @RequestParam(value = "status", required = false) Integer status,
	                             @RequestHeader("Authorization") String authorization) {
		Long studentId = getStudentId(authorization);
		if (studentId == null) {
			return R.fail(ResultCode.FAILED_UNAUTHORIZED);
		}
		return personalApplicationService.page(studentId, pageNum, pageSize, status);
	}

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


