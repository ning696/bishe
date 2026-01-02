package cn.zc.admin.service.enterprise;

import cn.zc.admin.domain.dto.EnterpriseStatusUpdateDTO;
import cn.zc.admin.domain.vo.EnterpriseDetailVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 企业管理服务接口
 *
 * @author
 */
public interface IEnterpriseService {

    /**
     * 分页查询企业列表
     */
    TableDataInfo queryEnterpriseList(Integer status, String keyword, Integer pageNum, Integer pageSize);

    /**
     * 企业详情
     */
    R<EnterpriseDetailVO> getEnterpriseDetail(Long enterpriseId);

    /**
     * 更新企业状态
     */
    R<Void> updateEnterpriseStatus(EnterpriseStatusUpdateDTO dto);
}

