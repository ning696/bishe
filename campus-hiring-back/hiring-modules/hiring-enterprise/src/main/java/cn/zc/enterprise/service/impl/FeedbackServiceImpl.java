package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.enterprise.domain.dto.FeedbackAddDTO;
import cn.zc.enterprise.domain.entity.Feedback;
import cn.zc.enterprise.mapper.FeedbackMapper;
import cn.zc.enterprise.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 反馈服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public R<Void> add(FeedbackAddDTO dto, Long enterpriseId) {
        Feedback feedback = new Feedback();
        feedback.setUserId(enterpriseId);
        feedback.setUserType(2); // 企业
        feedback.setFeedbackType(dto.getFeedbackType());
        feedback.setTitle(dto.getTitle());
        feedback.setContent(dto.getContent());
        feedback.setContactInfo(dto.getContactInfo());
        feedback.setHandleStatus(0); // 待处理

        int rows = feedbackMapper.insert(feedback);
        return rows > 0 ? R.ok() : R.fail();
    }
}

