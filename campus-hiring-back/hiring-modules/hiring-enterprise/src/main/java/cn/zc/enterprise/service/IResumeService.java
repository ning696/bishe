package cn.zc.enterprise.service;

import cn.zc.api.domain.vo.ResumeDetailVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.ResumeApplicationStatusUpdateDTO;

/**
 * 简历服务接口
 * 
 * @author campus-hiring-system
 */
public interface IResumeService {

    /**
     * 简历列表查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, Long jobId,
                         Integer applicationStatus, String keyword, Long enterpriseId);

    /**
     * 简历详情查询
     */
    R<ResumeDetailVO> detail(Long resumeId, Long applicationId);

    /**
     * 更新简历申请状态
     */
    R<Void> updateStatus(ResumeApplicationStatusUpdateDTO dto);
}











