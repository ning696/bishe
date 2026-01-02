package cn.zc.enterprise.service.impl;

import cn.zc.api.domain.vo.ResumeDetailVO;
import cn.zc.api.feign.RemoteStudentService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.ResumeApplicationStatusUpdateDTO;
import cn.zc.enterprise.domain.entity.JobApplication;
import cn.zc.enterprise.domain.vo.ResumeApplicationListVO;
import cn.zc.enterprise.mapper.JobApplicationMapper;
import cn.zc.enterprise.service.IResumeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 简历服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ResumeServiceImpl implements IResumeService {

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private RemoteStudentService remoteStudentService;

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, Long jobId,
                                Integer applicationStatus, String keyword, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ResumeApplicationListVO> list = jobApplicationMapper.selectResumeApplicationList(
                enterpriseId, jobId, applicationStatus, keyword);
        PageInfo<ResumeApplicationListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<ResumeDetailVO> detail(Long resumeId, Long applicationId) {
        // 通过 Feign 调用学生服务获取简历详情
        R<ResumeDetailVO> result = remoteStudentService.getResumeDetail(resumeId);
        
        if (result.getCode() != 1000 || result.getData() == null) {
            return result;
        }
        
        ResumeDetailVO resumeDetail = result.getData();
        
        // 获取学生信息
        if (resumeDetail.getStudentId() != null) {
            R<cn.zc.api.domain.vo.StudentInfoVO> studentResult = remoteStudentService.getStudentInfo(resumeDetail.getStudentId());
            if (studentResult.getCode() == 1000 && studentResult.getData() != null) {
                cn.zc.api.domain.vo.StudentInfoVO studentInfo = studentResult.getData();
                resumeDetail.setStudentName(studentInfo.getRealName());
                resumeDetail.setStudentPhone(studentInfo.getPhone());
                resumeDetail.setStudentEmail(studentInfo.getEmail());
            }
        }
        
        // 如果传入了 applicationId，获取投递状态并更新查看时间（首次查看）
        if (applicationId != null) {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application != null) {
                // 设置投递状态
                resumeDetail.setDeliveryStatus(application.getApplicationStatus());
                // 设置投递状态名称
                String statusName = getApplicationStatusName(application.getApplicationStatus());
                resumeDetail.setDeliveryStatusName(statusName);
                // 设置查看时间
                resumeDetail.setViewTime(application.getViewTime());
                
                // 如果首次查看，更新查看时间
                if (application.getViewTime() == null) {
                    application.setViewTime(LocalDateTime.now());
                    // 如果状态是待处理，更新为已查看（状态1表示已通过，这里不改变状态，只更新查看时间）
                    jobApplicationMapper.updateById(application);
                }
            }
        }
        
        return R.ok(resumeDetail);
    }
    
    /**
     * 获取申请状态名称
     */
    private String getApplicationStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "待处理";
            case 1:
                return "已通过";
            case 2:
                return "已拒绝";
            case 3:
                return "已取消";
            default:
                return "未知";
        }
    }

    @Override
    public R<Void> updateStatus(ResumeApplicationStatusUpdateDTO dto) {
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
}











