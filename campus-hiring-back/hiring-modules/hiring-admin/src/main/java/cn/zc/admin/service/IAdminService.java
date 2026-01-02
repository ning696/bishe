package cn.zc.admin.service;

import cn.zc.admin.domain.dto.AdminLoginDTO;
import cn.zc.admin.domain.vo.AdminInfoVO;
import cn.zc.common.core.domain.R;

/**
 * 管理员服务接口
 * 
 * @author campus-hiring-system
 */
public interface IAdminService {

    /**
     * 管理员登录
     * 
     * @param loginDTO 登录DTO
     * @return JWT Token
     */
    R<String> login(AdminLoginDTO loginDTO);

    /**
     * 获取管理员信息
     * 
     * @param adminId 管理员ID
     * @return 管理员信息
     */
    R<AdminInfoVO> getInfo(Long adminId);

    /**
     * 退出登录
     * 
     * @param token JWT Token
     * @return 操作结果
     */
    R<Void> logout(String token);
}

