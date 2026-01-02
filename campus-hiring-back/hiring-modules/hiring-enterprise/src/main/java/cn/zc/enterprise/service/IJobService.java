package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.JobAddDTO;
import cn.zc.enterprise.domain.dto.JobUpdateDTO;
import cn.zc.enterprise.domain.vo.JobDetailVO;
import cn.zc.enterprise.domain.vo.JobListVO;

/**
 * 职位服务接口
 * 
 * @author campus-hiring-system
 */
public interface IJobService {

    /**
     * 职位列表查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer status, String jobName, Long enterpriseId);

    /**
     * 职位详情查询
     */
    R<JobDetailVO> getDetail(Long jobId, Long enterpriseId);

    /**
     * 发布职位
     */
    R<Long> add(JobAddDTO dto, Long enterpriseId);

    /**
     * 编辑职位
     */
    R<Void> update(JobUpdateDTO dto, Long enterpriseId);

    /**
     * 删除职位
     */
    R<Void> delete(Long jobId, Long enterpriseId);

    /**
     * 下线职位
     */
    R<Void> offline(Long jobId, Long enterpriseId);
}

