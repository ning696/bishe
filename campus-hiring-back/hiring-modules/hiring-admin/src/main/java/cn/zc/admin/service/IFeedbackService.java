package cn.zc.admin.service;

import cn.zc.admin.domain.dto.FeedbackHandleDTO;
import cn.zc.admin.domain.vo.FeedbackListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 反馈管理服务接口
 * 
 * @author campus-hiring-system
 */
public interface IFeedbackService {

    /**
     * 反馈列表查询
     */
    TableDataInfo getFeedbackList(Integer handleStatus, String feedbackType, Integer pageNum, Integer pageSize);

    /**
     * 处理反馈
     */
    R<Void> handleFeedback(FeedbackHandleDTO dto, Long adminId);
}

