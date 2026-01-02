package cn.zc.student.service;

import cn.zc.student.domain.dto.InterviewApplyDTO;
import cn.zc.student.domain.vo.InterviewDetailVO;
import cn.zc.student.domain.vo.InterviewEvaluationVO;
import cn.zc.student.domain.vo.InterviewListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

import java.util.List;

/**
 * 面试服务接口
 * 
 * @author campus-hiring-system
 */
public interface IInterviewService {

    /**
     * 面试申请
     */
    R<Void> apply(InterviewApplyDTO dto, Long studentId);

    /**
     * 申请记录查询
     */
    R<List<InterviewListVO>> getApplicationList(Long studentId);

    /**
     * 面试安排查询
     */
    R<List<InterviewListVO>> getInterviewList(Long studentId);

    /**
     * 面试详情查询
     */
    R<InterviewDetailVO> getDetail(Long interviewId, Long studentId);

    /**
     * 面试评价查看
     */
    R<InterviewEvaluationVO> getEvaluation(Long interviewId, Long studentId);

    /**
     * 申请记录分页查询
     */
    R<TableDataInfo> pageApplication(Long studentId, Integer pageNum, Integer pageSize, Integer status);
}

