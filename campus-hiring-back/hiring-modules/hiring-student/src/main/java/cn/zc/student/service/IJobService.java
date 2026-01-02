package cn.zc.student.service;

import cn.zc.student.domain.dto.JobFavoriteDTO;
import cn.zc.student.domain.dto.JobSearchDTO;
import cn.zc.student.domain.vo.JobDetailVO;
import cn.zc.student.domain.vo.JobListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

import java.util.List;

/**
 * 职位服务接口（学生端）
 * 
 * @author campus-hiring-system
 */
public interface IJobService {

    /**
     * 职位列表查询（支持搜索和筛选）
     */
    R<TableDataInfo> list(JobSearchDTO dto, Long studentId);

    /**
     * 职位详情查询
     */
    R<JobDetailVO> getDetail(Long jobId, Long studentId);

    /**
     * 职位推荐
     */
    R<List<JobListVO>> getRecommended(Long studentId);

    /**
     * 收藏职位
     */
    R<Void> favorite(JobFavoriteDTO dto, Long studentId);

    /**
     * 取消收藏
     */
    R<Void> unfavorite(Long jobId, Long studentId);

    /**
     * 收藏列表查询
     */
    R<TableDataInfo> getFavoriteList(JobSearchDTO dto, Long studentId);
}

