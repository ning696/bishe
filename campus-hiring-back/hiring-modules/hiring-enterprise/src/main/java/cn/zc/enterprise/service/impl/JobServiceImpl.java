package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.PageQueryDTO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.JobAddDTO;
import cn.zc.enterprise.domain.dto.JobUpdateDTO;
import cn.zc.enterprise.domain.entity.CampusJob;
import cn.zc.enterprise.domain.entity.Job;
import cn.zc.enterprise.domain.vo.JobDetailVO;
import cn.zc.enterprise.domain.vo.JobListVO;
import cn.zc.enterprise.mapper.CampusJobMapper;
import cn.zc.enterprise.mapper.JobMapper;
import cn.zc.enterprise.service.IJobService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 职位服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private CampusJobMapper campusJobMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer status, String jobName, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobListVO> list = jobMapper.selectJobList(enterpriseId, status, jobName);
        PageInfo<JobListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<JobDetailVO> getDetail(Long jobId, Long enterpriseId) {
        JobDetailVO detail = jobMapper.selectJobDetail(jobId, enterpriseId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    @Transactional
    public R<Long> add(JobAddDTO dto, Long enterpriseId) {
        // 创建职位实体
        Job job = new Job();
        BeanUtils.copyProperties(dto, job);
        job.setEnterpriseId(enterpriseId);
        job.setStatus(0); // 待审核
        job.setViewCount(0);
        job.setApplyCount(0);
        
        // 处理过期时间
        if (dto.getExpireTime() != null && !dto.getExpireTime().isEmpty()) {
            job.setExpireTime(LocalDateTime.parse(dto.getExpireTime(), DATE_TIME_FORMATTER));
        }

        // 插入职位
        int rows = jobMapper.insert(job);
        if (rows <= 0) {
            return R.fail();
        }

        // 处理校园关联
        if (dto.getCampusIds() != null && !dto.getCampusIds().isEmpty()) {
            for (Long campusId : dto.getCampusIds()) {
                CampusJob campusJob = new CampusJob();
                campusJob.setCampusId(campusId);
                campusJob.setJobId(job.getId());
                campusJobMapper.insert(campusJob);
            }
        }

        return R.ok(job.getId());
    }

    @Override
    @Transactional
    public R<Void> update(JobUpdateDTO dto, Long enterpriseId) {
        // 检查职位是否存在且属于该企业
        Job existJob = jobMapper.selectById(dto.getJobId());
        if (existJob == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        if (!existJob.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 更新职位信息
        Job job = new Job();
        BeanUtils.copyProperties(dto, job);
        job.setId(dto.getJobId());
        
        // 处理过期时间
        if (dto.getExpireTime() != null && !dto.getExpireTime().isEmpty()) {
            job.setExpireTime(LocalDateTime.parse(dto.getExpireTime(), DATE_TIME_FORMATTER));
        }

        int rows = jobMapper.updateById(job);
        if (rows <= 0) {
            return R.fail();
        }

        // 处理校园关联：先删除旧的关联，再插入新的
        if (dto.getCampusIds() != null) {
            // 删除旧的关联
            LambdaQueryWrapper<CampusJob> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CampusJob::getJobId, dto.getJobId());
            campusJobMapper.delete(deleteWrapper);

            // 插入新的关联
            if (!dto.getCampusIds().isEmpty()) {
                for (Long campusId : dto.getCampusIds()) {
                    CampusJob campusJob = new CampusJob();
                    campusJob.setCampusId(campusId);
                    campusJob.setJobId(dto.getJobId());
                    campusJobMapper.insert(campusJob);
                }
            }
        }

        return R.ok();
    }

    @Override
    @Transactional
    public R<Void> delete(Long jobId, Long enterpriseId) {
        // 检查职位是否存在且属于该企业
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        if (!job.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 删除校园关联
        LambdaQueryWrapper<CampusJob> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CampusJob::getJobId, jobId);
        campusJobMapper.delete(deleteWrapper);

        // 删除职位（逻辑删除）
        int rows = jobMapper.deleteById(jobId);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> offline(Long jobId, Long enterpriseId) {
        // 检查职位是否存在且属于该企业
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        if (!job.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 更新状态为已下线
        LambdaUpdateWrapper<Job> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Job::getId, jobId)
                .set(Job::getStatus, 3); // 已下线
        int rows = jobMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

