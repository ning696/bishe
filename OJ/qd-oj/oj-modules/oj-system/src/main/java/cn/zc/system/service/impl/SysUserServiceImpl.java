package cn.zc.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zc.common.core.constants.HttpConstants;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.vo.LoginUserVO;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.common.core.enums.UserIdentity;
import cn.zc.security.exception.ServiceException;
import cn.zc.security.service.TokenService;
import cn.zc.system.domain.dto.LoginDTO;
import cn.zc.system.domain.dto.SysUserSaveDTO;
import cn.zc.system.domain.po.SysUser;
import cn.zc.system.mapper.SysUserMapper;
import cn.zc.system.service.ISysUserService;
import cn.zc.system.util.BCryptUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张策
 * @since 2024-11-20
 */
@Service
@RefreshScope
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TokenService tokenService;
    @Value("${jwt.secret}")
    private String secret;
    @Override
    public R<String> login(LoginDTO loginDTO) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        SysUser sysUser = sysUserMapper.selectOne(sysUserLambdaQueryWrapper.select(
                SysUser::getPassword,SysUser::getUserAccount,
                SysUser::getNickName).eq(SysUser::getUserAccount, loginDTO.getUserAccount()));
        if(sysUser==null){
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }else if (BCryptUtils.matchesPassword(loginDTO.getPassword(), sysUser.getPassword())){
            String token = tokenService.createToken(sysUser.getUserAccount(), secret, UserIdentity.ADMIN.getValue(),sysUser.getNickName());
            return R.ok(token);
        }
        return R.fail(ResultCode.FAILED_LOGIN);
    }

    @Override
    public int add(SysUserSaveDTO saveDTO) {
        if (!sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserAccount, saveDTO.getUserAccount())).isEmpty()){
            throw new ServiceException(ResultCode.AILED_USER_EXISTS);
        }
        return sysUserMapper.insert(new SysUser(saveDTO.getUserAccount(),
                BCryptUtils.encryptPassword(saveDTO.getPassword())
        ));
    }

    @Override
    public R<LoginUserVO> info(String token) {
        if (StrUtil.isNotEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            token = token.replaceFirst(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        LoginUser loginUser = tokenService.getLoginUser(token, secret);
        if (loginUser== null) {
            return R.fail();
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setNickName(loginUser.getNickName());
        return R.ok(loginUserVO);
    }
}
