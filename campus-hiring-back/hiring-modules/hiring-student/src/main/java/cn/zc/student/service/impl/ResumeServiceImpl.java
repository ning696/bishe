package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.ResumeAddDTO;
import cn.zc.student.domain.dto.ResumeDeliveryDTO;
import cn.zc.student.domain.dto.ResumeUpdateDTO;
import cn.zc.student.domain.entity.JobApplication;
import cn.zc.student.domain.entity.Resume;
import cn.zc.student.domain.vo.ResumeDetailVO;
import cn.zc.student.domain.vo.ResumeListVO;
import cn.zc.student.mapper.JobApplicationMapper;
import cn.zc.student.mapper.ResumeMapper;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 简历服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ResumeServiceImpl implements cn.zc.student.service.IResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Override
    public R<List<ResumeListVO>> list(Long studentId) {
        LambdaQueryWrapper<Resume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resume::getStudentId, studentId)
                .orderByDesc(Resume::getIsDefault)
                .orderByDesc(Resume::getCreateTime);

        List<Resume> resumes = resumeMapper.selectList(queryWrapper);
        List<ResumeListVO> voList = resumes.stream().map(resume -> {
            ResumeListVO vo = new ResumeListVO();
            BeanUtils.copyProperties(resume, vo);
            return vo;
        }).collect(Collectors.toList());

        return R.ok(voList);
    }

    @Override
    public R<ResumeDetailVO> getDetail(Long resumeId, Long studentId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        if (!resume.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        ResumeDetailVO vo = new ResumeDetailVO();
        BeanUtils.copyProperties(resume, vo);
        return R.ok(vo);
    }

    @Override
    public R<ResumeDetailVO> getDetailWithoutAuth(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        ResumeDetailVO vo = new ResumeDetailVO();
        BeanUtils.copyProperties(resume, vo);
        return R.ok(vo);
    }

    @Override
    @Transactional
    public R<Void> add(ResumeAddDTO dto, Long studentId) {
        // 如果设置为默认简历，则取消其他默认简历
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Resume::getStudentId, studentId)
                    .eq(Resume::getIsDefault, 1)
                    .set(Resume::getIsDefault, 0);
            resumeMapper.update(null, updateWrapper);
        }

        Resume resume = new Resume();
        BeanUtils.copyProperties(dto, resume);
        resume.setStudentId(studentId);
        resume.setStatus(1); // 正常状态

        int rows = resumeMapper.insert(resume);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    @Transactional
    public R<Void> update(ResumeUpdateDTO dto, Long studentId) {
        Resume resume = resumeMapper.selectById(dto.getResumeId());
        if (resume == null) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        if (!resume.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 如果设置为默认简历，则取消其他默认简历
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Resume::getStudentId, studentId)
                    .eq(Resume::getIsDefault, 1)
                    .ne(Resume::getId, dto.getResumeId())
                    .set(Resume::getIsDefault, 0);
            resumeMapper.update(null, updateWrapper);
        }

        LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getId, dto.getResumeId());

        if (dto.getResumeName() != null) {
            updateWrapper.set(Resume::getResumeName, dto.getResumeName());
        }
        if (dto.getResumeFile() != null) {
            updateWrapper.set(Resume::getResumeFile, dto.getResumeFile());
        }
        if (dto.getPersonalInfo() != null) {
            updateWrapper.set(Resume::getPersonalInfo, dto.getPersonalInfo());
        }
        if (dto.getEducationBackground() != null) {
            updateWrapper.set(Resume::getEducationBackground, dto.getEducationBackground());
        }
        if (dto.getWorkExperience() != null) {
            updateWrapper.set(Resume::getWorkExperience, dto.getWorkExperience());
        }
        if (dto.getProjectExperience() != null) {
            updateWrapper.set(Resume::getProjectExperience, dto.getProjectExperience());
        }
        if (dto.getSkills() != null) {
            updateWrapper.set(Resume::getSkills, dto.getSkills());
        }
        if (dto.getSelfIntroduction() != null) {
            updateWrapper.set(Resume::getSelfIntroduction, dto.getSelfIntroduction());
        }
        if (dto.getIsDefault() != null) {
            updateWrapper.set(Resume::getIsDefault, dto.getIsDefault());
        }

        int rows = resumeMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    @Transactional
    public R<Void> delete(Long resumeId, Long studentId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        if (!resume.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        int rows = resumeMapper.deleteById(resumeId);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    @Transactional
    public R<Void> delivery(ResumeDeliveryDTO dto, Long studentId) {
        // 检查简历是否存在且属于该学生
        Resume resume = resumeMapper.selectById(dto.getResumeId());
        if (resume == null) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        if (!resume.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }

        // 检查是否已投递过该职位
        LambdaQueryWrapper<JobApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobApplication::getStudentId, studentId)
                .eq(JobApplication::getJobId, dto.getJobId());
        JobApplication exist = jobApplicationMapper.selectOne(queryWrapper);

        if (exist != null) {
            return R.fail(ResultCode.FAILED_RESUME_ALREADY_APPLIED);
        }

        // 创建职位申请记录
        JobApplication application = new JobApplication();
        application.setStudentId(studentId);
        application.setJobId(dto.getJobId());
        application.setResumeId(dto.getResumeId());
        application.setApplicationStatus(0); // 待处理
        application.setApplicationTime(LocalDateTime.now());

        int rows = jobApplicationMapper.insert(application);
        return rows > 0 ? R.ok() : R.fail();
    }
}

