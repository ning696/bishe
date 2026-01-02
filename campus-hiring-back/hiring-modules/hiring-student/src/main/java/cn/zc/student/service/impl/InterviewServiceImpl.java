package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.InterviewApplyDTO;
import cn.zc.student.domain.entity.Interview;
import cn.zc.student.domain.entity.InterviewEvaluation;
import cn.zc.student.domain.entity.JobApplication;
import cn.zc.student.domain.vo.InterviewDetailVO;
import cn.zc.student.domain.vo.InterviewEvaluationVO;
import cn.zc.student.domain.vo.InterviewListVO;
import cn.zc.student.mapper.InterviewEvaluationMapper;
import cn.zc.student.mapper.InterviewMapper;
import cn.zc.student.mapper.JobApplicationMapper;
import cn.zc.student.service.IInterviewService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 面试服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class InterviewServiceImpl implements IInterviewService {

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private InterviewEvaluationMapper interviewEvaluationMapper;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Override
    @Transactional
    public R<Void> apply(InterviewApplyDTO dto, Long studentId) {
        // 检查是否已有职位申请记录
        LambdaQueryWrapper<JobApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobApplication::getStudentId, studentId)
                .eq(JobApplication::getJobId, dto.getJobId());
        JobApplication application = jobApplicationMapper.selectOne(queryWrapper);

        if (application == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 检查是否已申请过面试
        LambdaQueryWrapper<Interview> interviewQuery = new LambdaQueryWrapper<>();
        interviewQuery.eq(Interview::getStudentId, studentId)
                .eq(Interview::getJobId, dto.getJobId());
        Interview exist = interviewMapper.selectOne(interviewQuery);

        if (exist != null) {
            return R.fail(ResultCode.FAILED_ALREADY_EXISTS);
        }

        // 创建面试申请
        Interview interview = new Interview();
        interview.setStudentId(studentId);
        interview.setJobId(dto.getJobId());
        interview.setApplicationId(application.getId());
        interview.setInterviewStatus(0); // 待安排

        int rows = interviewMapper.insert(interview);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<List<InterviewListVO>> getApplicationList(Long studentId) {
        List<InterviewListVO> list = interviewMapper.selectInterviewList(studentId);
        return R.ok(list);
    }

    @Override
    public R<TableDataInfo> pageApplication(Long studentId, Integer pageNum, Integer pageSize, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<InterviewListVO> list = interviewMapper.selectInterviewPage(studentId, status);
        PageInfo<InterviewListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<List<InterviewListVO>> getInterviewList(Long studentId) {
        // 查询已安排的面试（状态为1-已安排）
        List<InterviewListVO> list = interviewMapper.selectInterviewList(studentId);
        // 过滤出已安排的面试
        List<InterviewListVO> arrangedList = list.stream()
                .filter(item -> item.getInterviewStatus() != null && item.getInterviewStatus() >= 1)
                .toList();
        return R.ok(arrangedList);
    }

    @Override
    public R<InterviewDetailVO> getDetail(Long interviewId, Long studentId) {
        InterviewDetailVO detail = interviewMapper.selectInterviewDetail(interviewId, studentId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_INTERVIEW_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<InterviewEvaluationVO> getEvaluation(Long interviewId, Long studentId) {
        // 先检查面试是否存在且属于该学生
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            return R.fail(ResultCode.FAILED_INTERVIEW_NOT_EXISTS);
        }

        if (!interview.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 查询面试评价
        LambdaQueryWrapper<InterviewEvaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewEvaluation::getInterviewId, interviewId);
        InterviewEvaluation evaluation = interviewEvaluationMapper.selectOne(queryWrapper);

        if (evaluation == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        InterviewEvaluationVO vo = new InterviewEvaluationVO();
        BeanUtils.copyProperties(evaluation, vo);
        return R.ok(vo);
    }
}

