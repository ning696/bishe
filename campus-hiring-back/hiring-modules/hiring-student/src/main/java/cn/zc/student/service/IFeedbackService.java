package cn.zc.student.service;

import cn.zc.common.core.domain.R;
import cn.zc.student.domain.dto.FeedbackAddDTO;

/**
 * 反馈服务接口
 * 
 * @author campus-hiring-system
 */
public interface IFeedbackService {

    /**
     * 提交反馈
     */
    R<Void> add(FeedbackAddDTO dto, Long studentId);
}

