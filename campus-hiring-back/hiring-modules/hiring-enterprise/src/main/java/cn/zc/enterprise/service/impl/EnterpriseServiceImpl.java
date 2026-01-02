package cn.zc.enterprise.service.impl;

import cn.zc.enterprise.domain.dto.*;
import cn.zc.enterprise.domain.entity.Enterprise;
import cn.zc.enterprise.domain.vo.EnterpriseDetailVO;
import cn.zc.enterprise.domain.vo.EnterpriseInfoVO;
import cn.zc.enterprise.mapper.EnterpriseMapper;
import cn.zc.enterprise.service.IEnterpriseService;
import cn.zc.minio.domain.OSSResult;
import cn.zc.minio.service.MinioService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.common.core.enums.UserIdentity;
import cn.zc.common.core.util.PasswordUtils;
import cn.zc.security.service.TokenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 企业服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class EnterpriseServiceImpl implements IEnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MinioService minioService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public R<Void> register(EnterpriseRegisterDTO dto) {
        // 基础参数校验
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        if (dto.getEnterpriseName() == null || dto.getEnterpriseName().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<Enterprise> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Enterprise::getUsername, dto.getUsername());
        Enterprise existEnterprise = enterpriseMapper.selectOne(queryWrapper);
        if (existEnterprise != null) {
            return R.fail(ResultCode.FAILED_USER_EXISTS);
        }

        // 取消验证码与手机号校验（按当前注册简化规则，不需要）

        // 创建企业
        Enterprise enterprise = new Enterprise();
        enterprise.setUsername(dto.getUsername());
        enterprise.setPassword(PasswordUtils.encode(dto.getPassword()));
        enterprise.setEnterpriseName(dto.getEnterpriseName());
        enterprise.setStatus(1); // 正常状态
        enterprise.setCertificationStatus(0); // 未认证

        int rows = enterpriseMapper.insert(enterprise);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> sendCode(SendCodeDTO dto) {
        // TODO: 发送手机验证码（这里简化处理，实际应该调用短信服务）
        // 验证码应该存储到Redis中，设置过期时间
        return R.ok();
    }

    @Override
    public R<String> login(EnterpriseLoginDTO dto) {
        // 参数校验
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 根据用户名查询企业
        LambdaQueryWrapper<Enterprise> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Enterprise::getUsername, dto.getUsername());
        Enterprise enterprise = enterpriseMapper.selectOne(queryWrapper);

        if (enterprise == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        // 验证密码
//        if (!PasswordUtils.matches(dto.getPassword(), enterprise.getPassword())) {
//            return R.fail(ResultCode.FAILED_LOGIN);
//        }

        // 检查状态
        if (enterprise.getStatus() == 0) {
            return R.fail(ResultCode.FAILED_USER_BANNED);
        }

        // 生成Token
        String token = tokenService.createToken(
                enterprise.getId(),
                secret,
                UserIdentity.ENTERPRISE.getValue(),
                enterprise.getEnterpriseName(),
                enterprise.getLogo()
        );

        return R.ok(token);
    }

    @Override
    public R<EnterpriseInfoVO> getInfo(Long enterpriseId) {
        Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
        if (enterprise == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        EnterpriseInfoVO vo = new EnterpriseInfoVO();
        vo.setEnterpriseName(enterprise.getEnterpriseName());
        vo.setLogo(enterprise.getLogo());
        return R.ok(vo);
    }

    @Override
    public R<EnterpriseDetailVO> getDetail(Long enterpriseId) {
        EnterpriseDetailVO detail = enterpriseMapper.selectEnterpriseDetail(enterpriseId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<Void> updateInfo(EnterpriseUpdateDTO dto, Long enterpriseId) {
        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, enterpriseId);

        if (dto.getEnterpriseName() != null) {
            updateWrapper.set(Enterprise::getEnterpriseName, dto.getEnterpriseName());
        }
        if (dto.getLegalPerson() != null) {
            updateWrapper.set(Enterprise::getLegalPerson, dto.getLegalPerson());
        }
        if (dto.getPhone() != null) {
            updateWrapper.set(Enterprise::getPhone, dto.getPhone());
        }
        if (dto.getEmail() != null) {
            updateWrapper.set(Enterprise::getEmail, dto.getEmail());
        }
        if (dto.getAddress() != null) {
            updateWrapper.set(Enterprise::getAddress, dto.getAddress());
        }
        if (dto.getIndustry() != null) {
            updateWrapper.set(Enterprise::getIndustry, dto.getIndustry());
        }
        if (dto.getScale() != null) {
            updateWrapper.set(Enterprise::getScale, dto.getScale());
        }
        if (dto.getDescription() != null) {
            updateWrapper.set(Enterprise::getDescription, dto.getDescription());
        }
        if (dto.getWebsite() != null) {
            updateWrapper.set(Enterprise::getWebsite, dto.getWebsite());
        }

        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> changePassword(PasswordChangeDTO dto, Long enterpriseId) {
        Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
        if (enterprise == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        if (!PasswordUtils.matches(dto.getOldPassword(), enterprise.getPassword())) {
            return R.fail(ResultCode.FAILED_ERROR_PASSWORD);
        }

        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, enterpriseId)
                .set(Enterprise::getPassword, PasswordUtils.encode(dto.getNewPassword()));

        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> updateLogo(LogoUpdateDTO dto, Long enterpriseId) {
        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, enterpriseId)
                .set(Enterprise::getLogo, dto.getLogo());

        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<OSSResult> uploadFile(MultipartFile file, Long enterpriseId) {
        if (file == null || file.isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        String originalFilename = file.getOriginalFilename();
        String objectName = "enterprise/" + enterpriseId + "/" + System.currentTimeMillis();
        if (StringUtils.hasText(originalFilename)) {
            objectName = objectName + "_" + originalFilename;
        }

        OSSResult ossResult = minioService.uploadFile(file, bucketName, objectName);
        return R.ok(ossResult);
    }

    @Override
    public R<Void> applyCertification(MultipartFile file, Long enterpriseId) {
        // 参数校验
        if (file == null || file.isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 构建文件存储路径
        String originalFilename = file.getOriginalFilename();
        String objectName = "certificate/" + enterpriseId + "/" + System.currentTimeMillis();
        if (StringUtils.hasText(originalFilename)) {
            objectName = objectName + "_" + originalFilename;
        }

        // 上传文件到MinIO
        OSSResult ossResult = minioService.uploadFile(file, bucketName, objectName);
        if (ossResult == null || !ossResult.isSuccess()) {
            return R.fail(ResultCode.FAILED_FILE_UPLOAD);
        }

        // 获取文件URL并保存到数据库
        String certificationFileUrl = ossResult.getName();
        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, enterpriseId)
                .set(Enterprise::getCertificationFile, certificationFileUrl)
                .set(Enterprise::getCertificationStatus, 2); // 认证中

        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> logout(String token) {
        boolean result = tokenService.deleteLoginUser(token, secret);
        return result ? R.ok() : R.fail();
    }
}

