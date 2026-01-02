package cn.zc.admin.service;

import cn.zc.admin.domain.dto.EnterpriseCertificationAuditDTO;
import cn.zc.admin.domain.vo.EnterpriseCertificationListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 企业认证服务接口
 * 
 * @author campus-hiring-system
 */
public interface IEnterpriseCertificationService {

    /**
     * 企业认证列表查询
     */
    TableDataInfo getCertificationList(Integer certificationStatus, String enterpriseName, Integer pageNum, Integer pageSize);

    /**
     * 企业认证审核
     */
    R<Void> auditCertification(EnterpriseCertificationAuditDTO dto);
}

