package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.EnterpriseCertificationAuditDTO;
import cn.zc.admin.domain.entity.Enterprise;
import cn.zc.admin.domain.vo.EnterpriseCertificationListVO;
import cn.zc.admin.mapper.EnterpriseMapper;
import cn.zc.admin.service.IEnterpriseCertificationService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业认证服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class EnterpriseCertificationServiceImpl implements IEnterpriseCertificationService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Override
    public TableDataInfo getCertificationList(Integer certificationStatus, String enterpriseName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EnterpriseCertificationListVO> list = enterpriseMapper.selectCertificationList(certificationStatus, enterpriseName);
        PageInfo<EnterpriseCertificationListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<Void> auditCertification(EnterpriseCertificationAuditDTO dto) {
        Enterprise enterprise = enterpriseMapper.selectById(dto.getEnterpriseId());
        if (enterprise == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, dto.getEnterpriseId())
                .set(Enterprise::getCertificationStatus, dto.getCertificationStatus());

        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

