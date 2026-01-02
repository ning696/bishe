package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.JobAuditDTO;
import cn.zc.admin.domain.entity.Job;
import cn.zc.admin.domain.vo.JobDetailVO;
import cn.zc.admin.domain.vo.JobListVO;
import cn.zc.admin.mapper.JobMapper;
import cn.zc.admin.service.IJobService;
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
 * 职位审核服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public TableDataInfo getJobList(Integer status, Long enterpriseId, String jobName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobListVO> list = jobMapper.selectJobList(status, enterpriseId, jobName);
        PageInfo<JobListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<JobDetailVO> getJobDetail(Long jobId) {
        JobDetailVO detail = jobMapper.selectJobDetail(jobId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<Void> auditJob(JobAuditDTO dto, Long adminId) {
        Job job = jobMapper.selectById(dto.getJobId());
        if (job == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        LambdaUpdateWrapper<Job> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Job::getId, dto.getJobId())
                .set(Job::getStatus, dto.getStatus())
                .set(Job::getAuditRemark, dto.getAuditRemark())
                .set(Job::getAuditTime, LocalDateTime.now())
                .set(Job::getAuditBy, adminId);

        // 如果审核通过，设置发布时间
        if (dto.getStatus() == 1 && job.getPublishTime() == null) {
            updateWrapper.set(Job::getPublishTime, LocalDateTime.now());
        }

        int rows = jobMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

