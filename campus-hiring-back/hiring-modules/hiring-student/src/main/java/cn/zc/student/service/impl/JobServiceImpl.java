package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.JobFavoriteDTO;
import cn.zc.student.domain.dto.JobSearchDTO;
import cn.zc.student.domain.entity.JobApplication;
import cn.zc.student.domain.entity.JobFavorite;
import cn.zc.student.domain.vo.JobDetailVO;
import cn.zc.student.domain.vo.JobListVO;
import cn.zc.student.mapper.JobApplicationMapper;
import cn.zc.student.mapper.JobFavoriteMapper;
import cn.zc.student.mapper.JobMapper;
import cn.zc.student.service.IJobService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 职位服务实现类（学生端）
 * 
 * @author campus-hiring-system
 */
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobFavoriteMapper jobFavoriteMapper;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Override
    public R<TableDataInfo> list(JobSearchDTO dto, Long studentId) {
        List<JobListVO> list = jobMapper.selectJobListForStudent(dto, studentId);
        
        // 手动分页（因为使用了自定义SQL）
        int pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<JobListVO> pageList = start < total ? list.subList(start, end) : List.of();
        
        TableDataInfo dataTable = new TableDataInfo();
        dataTable.setRows(pageList);
        dataTable.setTotal(total);
        return R.ok(dataTable);
    }

    @Override
    public R<JobDetailVO> getDetail(Long jobId, Long studentId) {
        JobDetailVO detail = jobMapper.selectJobDetailForStudent(jobId, studentId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_JOB_NOT_EXISTS);
        }

        // 增加浏览次数
        // TODO: 这里应该更新 job 表的 view_count，但需要访问 admin 模块的 JobMapper
        // 暂时跳过，或者可以通过 Feign 调用 admin 模块的接口

        return R.ok(detail);
    }

    @Override
    public R<List<JobListVO>> getRecommended(Long studentId) {
        List<JobListVO> list = jobMapper.selectRecommendedJobs(studentId, 10);
        return R.ok(list);
    }

    @Override
    @Transactional
    public R<Void> favorite(JobFavoriteDTO dto, Long studentId) {
        // 检查是否已收藏
        LambdaQueryWrapper<JobFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobFavorite::getStudentId, studentId)
                .eq(JobFavorite::getJobId, dto.getJobId());
        JobFavorite exist = jobFavoriteMapper.selectOne(queryWrapper);
        
        if (exist != null) {
            return R.fail(ResultCode.FAILED_ALREADY_EXISTS);
        }

        // 添加收藏
        JobFavorite favorite = new JobFavorite();
        favorite.setStudentId(studentId);
        favorite.setJobId(dto.getJobId());
        favorite.setCreateTime(LocalDateTime.now());

        int rows = jobFavoriteMapper.insert(favorite);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    @Transactional
    public R<Void> unfavorite(Long jobId, Long studentId) {
        LambdaQueryWrapper<JobFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobFavorite::getStudentId, studentId)
                .eq(JobFavorite::getJobId, jobId);
        
        int rows = jobFavoriteMapper.delete(queryWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<TableDataInfo> getFavoriteList(JobSearchDTO dto, Long studentId) {
        List<JobListVO> list = jobMapper.selectFavoriteJobList(dto, studentId);
        
        // 手动分页（因为使用了自定义SQL）
        int pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<JobListVO> pageList = start < total ? list.subList(start, end) : List.of();
        
        TableDataInfo dataTable = new TableDataInfo();
        dataTable.setRows(pageList);
        dataTable.setTotal(total);
        return R.ok(dataTable);
    }
}

