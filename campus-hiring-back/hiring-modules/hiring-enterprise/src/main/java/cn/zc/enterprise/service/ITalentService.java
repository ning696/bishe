package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.vo.TalentRecommendVO;

import java.util.List;

/**
 * 人才推荐服务接口
 * 
 * @author campus-hiring-system
 */
public interface ITalentService {

    /**
     * 人才推荐
     */
    R<TableDataInfo> recommend(Long enterpriseId, Integer pageNum, Integer pageSize,
                               Long jobId, String requiredMajor, String requiredSkills,
                               String requiredEducation, Integer requiredExperience, Long campusId);

    /**
     * 首页指定职位的人才推荐
     */
    R<List<TalentRecommendVO>> recommendForJob(Long enterpriseId, Long jobId, Integer limit);
}











