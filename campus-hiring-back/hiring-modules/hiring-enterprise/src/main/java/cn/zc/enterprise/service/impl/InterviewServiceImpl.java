package cn.zc.enterprise.service.impl;

import cn.zc.api.feign.RemoteStudentService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.InterviewApplicationHandleDTO;
import cn.zc.enterprise.domain.dto.InterviewArrangeDTO;
import cn.zc.enterprise.domain.dto.InterviewEvaluationDTO;
import cn.zc.enterprise.domain.entity.Interview;
import cn.zc.enterprise.domain.entity.JobApplication;
import cn.zc.enterprise.domain.vo.InterviewListVO;
import cn.zc.enterprise.domain.vo.JobApplicationListVO;
import cn.zc.enterprise.mapper.InterviewMapper;
import cn.zc.enterprise.mapper.JobApplicationMapper;
import cn.zc.enterprise.service.IInterviewService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 面试服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class InterviewServiceImpl implements IInterviewService {

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private RemoteStudentService remoteStudentService;

    @Override
    public R<TableDataInfo> applicationList(Integer pageNum, Integer pageSize, Long jobId,
                                           Integer applicationStatus, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobApplicationListVO> list = jobApplicationMapper.selectJobApplicationList(
                enterpriseId, jobId, applicationStatus);
        PageInfo<JobApplicationListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<Void> handleApplication(InterviewApplicationHandleDTO dto) {
        JobApplication application = jobApplicationMapper.selectById(dto.getApplicationId());
        if (application == null) {
            return R.fail();
        }

        application.setApplicationStatus(dto.getApplicationStatus());
        application.setHandleTime(LocalDateTime.now());
        application.setHandleRemark(dto.getHandleRemark());

        int rows = jobApplicationMapper.updateById(application);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Long> arrange(InterviewArrangeDTO dto, Long enterpriseId) {
        // 创建面试记录
        Interview interview = new Interview();
        interview.setStudentId(dto.getStudentId());
        interview.setEnterpriseId(enterpriseId);
        interview.setJobId(dto.getJobId());
        interview.setApplicationId(dto.getApplicationId());
        
        // 解析面试时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        interview.setInterviewTime(LocalDateTime.parse(dto.getInterviewTime(), formatter));
        
        interview.setInterviewLocation(dto.getInterviewLocation());
        interview.setInterviewType(dto.getInterviewType());
        interview.setInterviewStatus(1); // 已安排
        interview.setContactPerson(dto.getContactPerson());
        interview.setContactPhone(dto.getContactPhone());
        interview.setRemark(dto.getRemark());

        int rows = interviewMapper.insert(interview);
        
        // TODO: 通过 Feign 调用学生服务创建面试记录
        // TODO: 发送 RabbitMQ 消息通知学生
        
        return rows > 0 ? R.ok(interview.getId()) : R.fail();
    }

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, Long jobId,
                                 Integer interviewStatus, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<InterviewListVO> list = interviewMapper.selectInterviewList(
                enterpriseId, jobId, interviewStatus);
        PageInfo<InterviewListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<Long> evaluation(InterviewEvaluationDTO dto, Long enterpriseId) {
        // 通过 Feign 调用学生服务提交面试评价
        cn.zc.api.domain.dto.InterviewEvaluationDTO apiDTO = new cn.zc.api.domain.dto.InterviewEvaluationDTO();
        BeanUtils.copyProperties(dto, apiDTO);
        apiDTO.setEnterpriseId(enterpriseId);
        
        R<Void> result = remoteStudentService.submitInterviewEvaluation(apiDTO);
        return result.getCode() == 1000 ? R.ok(1L) : R.fail();
    }
}



