package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.InterviewApplicationHandleDTO;
import cn.zc.enterprise.domain.dto.InterviewArrangeDTO;
import cn.zc.enterprise.domain.dto.InterviewEvaluationDTO;

/**
 * 面试服务接口
 * 
 * @author campus-hiring-system
 */
public interface IInterviewService {

    /**
     * 面试申请查看
     */
    R<TableDataInfo> applicationList(Integer pageNum, Integer pageSize, Long jobId,
                                     Integer applicationStatus, Long enterpriseId);

    /**
     * 处理面试申请
     */
    R<Void> handleApplication(InterviewApplicationHandleDTO dto);

    /**
     * 安排面试
     */
    R<Long> arrange(InterviewArrangeDTO dto, Long enterpriseId);

    /**
     * 面试列表查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, Long jobId,
                         Integer interviewStatus, Long enterpriseId);

    /**
     * 面试评价
     */
    R<Long> evaluation(InterviewEvaluationDTO dto, Long enterpriseId);
}











