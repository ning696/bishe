package cn.zc.system.service;

import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.vo.LoginUserVO;
import cn.zc.system.domain.dto.LoginDTO;
import cn.zc.system.domain.dto.SysUserSaveDTO;
import cn.zc.system.domain.po.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张策
 * @since 2024-11-20
 */
public interface ISysUserService extends IService<SysUser> {

    R<String> login(LoginDTO loginDTO);

    int add(SysUserSaveDTO saveDTO);

    R<LoginUserVO> info(String token);
}
