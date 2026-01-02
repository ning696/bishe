package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.FeedbackHandleDTO;
import cn.zc.admin.domain.entity.Feedback;
import cn.zc.admin.domain.vo.FeedbackListVO;
import cn.zc.admin.mapper.FeedbackMapper;
import cn.zc.admin.service.IFeedbackService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 反馈管理服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public TableDataInfo getFeedbackList(Integer handleStatus, String feedbackType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FeedbackListVO> list = feedbackMapper.selectFeedbackList(handleStatus, feedbackType);
        PageInfo<FeedbackListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<Void> handleFeedback(FeedbackHandleDTO dto, Long adminId) {
        Feedback feedback = feedbackMapper.selectById(dto.getFeedbackId());
        if (feedback == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        LambdaUpdateWrapper<Feedback> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Feedback::getId, dto.getFeedbackId())
                .set(Feedback::getHandleStatus, dto.getHandleStatus())
                .set(Feedback::getHandleResult, dto.getHandleResult())
                .set(Feedback::getHandleRemark, dto.getHandleRemark())
                .set(Feedback::getHandleTime, LocalDateTime.now())
                .set(Feedback::getHandleBy, adminId);

        int rows = feedbackMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

