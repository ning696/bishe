package cn.zc.student.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.student.domain.dto.FeedbackAddDTO;
import cn.zc.student.domain.entity.Feedback;
import cn.zc.student.mapper.FeedbackMapper;
import cn.zc.student.service.IFeedbackService;
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
    public R<Void> add(FeedbackAddDTO dto, Long studentId) {
        Feedback feedback = new Feedback();
        feedback.setUserId(studentId);
        feedback.setUserType(1); // 学生
        feedback.setFeedbackType(dto.getFeedbackType());
        feedback.setTitle(dto.getTitle());
        feedback.setContent(dto.getContent());
        feedback.setContactInfo(dto.getContactInfo());
        feedback.setHandleStatus(0); // 待处理

        int rows = feedbackMapper.insert(feedback);
        return rows > 0 ? R.ok() : R.fail();
    }
}

