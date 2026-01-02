package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.AdminLoginDTO;
import cn.zc.admin.domain.entity.Admin;
import cn.zc.admin.domain.vo.AdminInfoVO;
import cn.zc.admin.mapper.AdminMapper;
import cn.zc.admin.service.IAdminService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.common.core.enums.UserIdentity;
import cn.zc.common.core.util.PasswordUtils;
import cn.zc.security.service.TokenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 管理员服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public R<String> login(AdminLoginDTO loginDTO) {
        // 查询管理员
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, loginDTO.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);

        if (admin == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

//        // 验证密码
//        if (!PasswordUtils.matches(loginDTO.getPassword(), admin.getPassword())) {
//            return R.fail(ResultCode.FAILED_LOGIN);
//        }

        // 检查状态
        if (admin.getStatus() == 0) {
            return R.fail(ResultCode.FAILED_USER_BANNED);
        }

        // 生成Token
        String token = tokenService.createToken(
                admin.getId(),
                secret,
                UserIdentity.ADMIN.getValue(),
                admin.getNickName(),
                admin.getHeadImage()
        );

        return R.ok(token);
    }

    @Override
    public R<AdminInfoVO> getInfo(Long adminId) {
        Admin admin = adminMapper.selectById(adminId);
        if (admin == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        AdminInfoVO vo = new AdminInfoVO();
        BeanUtils.copyProperties(admin, vo);
        
        // 设置状态名称
        if (admin.getStatus() != null) {
            switch (admin.getStatus()) {
                case 0:
                    vo.setStatusName("已拉黑");
                    break;
                case 1:
                    vo.setStatusName("正常");
                    break;
                case 2:
                    vo.setStatusName("已禁用");
                    break;
                default:
                    vo.setStatusName("未知");
            }
        }

        return R.ok(vo);
    }

    @Override
    public R<Void> logout(String token) {
        boolean result = tokenService.deleteLoginUser(token, secret);
        return result ? R.ok() : R.fail();
    }
}

