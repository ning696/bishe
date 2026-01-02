package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.vo.TalentRecommendVO;
import cn.zc.enterprise.mapper.TalentMapper;
import cn.zc.enterprise.service.ITalentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人才推荐服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class TalentServiceImpl implements ITalentService {

    @Autowired
    private TalentMapper talentMapper;

    @Override
    public R<TableDataInfo> recommend(Long enterpriseId, Integer pageNum, Integer pageSize,
                                      Long jobId, String requiredMajor, String requiredSkills,
                                      String requiredEducation, Integer requiredExperience, Long campusId) {
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TalentRecommendVO> list = talentMapper.selectTalentRecommendList(
                enterpriseId, jobId, requiredMajor, requiredSkills, requiredEducation, requiredExperience, campusId);
        PageInfo<TalentRecommendVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<List<TalentRecommendVO>> recommendForJob(Long enterpriseId, Long jobId, Integer limit) {
        if (enterpriseId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        if (jobId == null) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        int finalLimit = (limit == null || limit <= 0) ? 6 : Math.min(limit, 20);
        List<TalentRecommendVO> list = talentMapper.selectRecommendedTalentForJob(enterpriseId, jobId, finalLimit);
        return R.ok(list);
    }
}











