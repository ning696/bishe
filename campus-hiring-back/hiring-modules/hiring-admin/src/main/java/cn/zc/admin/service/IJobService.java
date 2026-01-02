package cn.zc.admin.service;

import cn.zc.admin.domain.dto.JobAuditDTO;
import cn.zc.admin.domain.vo.JobDetailVO;
import cn.zc.admin.domain.vo.JobListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 职位审核服务接口
 * 
 * @author campus-hiring-system
 */
public interface IJobService {

    /**
     * 职位列表查询
     */
    TableDataInfo getJobList(Integer status, Long enterpriseId, String jobName, Integer pageNum, Integer pageSize);

    /**
     * 职位详情查询
     */
    R<JobDetailVO> getJobDetail(Long jobId);

    /**
     * 职位审核
     */
    R<Void> auditJob(JobAuditDTO dto, Long adminId);
}

