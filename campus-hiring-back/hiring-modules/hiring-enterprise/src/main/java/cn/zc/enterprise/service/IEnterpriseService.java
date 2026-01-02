package cn.zc.enterprise.service;

import cn.zc.enterprise.domain.dto.*;
import cn.zc.enterprise.domain.vo.EnterpriseDetailVO;
import cn.zc.enterprise.domain.vo.EnterpriseInfoVO;
import cn.zc.minio.domain.OSSResult;
import cn.zc.common.core.domain.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * 企业服务接口
 * 
 * @author campus-hiring-system
 */
public interface IEnterpriseService {

    /**
     * 企业注册
     */
    R<Void> register(EnterpriseRegisterDTO dto);

    /**
     * 发送验证码
     */
    R<Void> sendCode(SendCodeDTO dto);

    /**
     * 企业登录
     */
    R<String> login(EnterpriseLoginDTO dto);

    /**
     * 获取企业信息
     */
    R<EnterpriseInfoVO> getInfo(Long enterpriseId);

    /**
     * 企业详情查询
     */
    R<EnterpriseDetailVO> getDetail(Long enterpriseId);

    /**
     * 更新企业信息
     */
    R<Void> updateInfo(EnterpriseUpdateDTO dto, Long enterpriseId);

    /**
     * 修改密码
     */
    R<Void> changePassword(PasswordChangeDTO dto, Long enterpriseId);

    /**
     * 更新企业Logo
     */
    R<Void> updateLogo(LogoUpdateDTO dto, Long enterpriseId);

    /**
     * 企业文件上传
     */
    R<OSSResult> uploadFile(MultipartFile file, Long enterpriseId);

    /**
     * 企业认证申请
     */
    R<Void> applyCertification(MultipartFile file, Long enterpriseId);

    /**
     * 退出登录
     */
    R<Void> logout(String token);
}

