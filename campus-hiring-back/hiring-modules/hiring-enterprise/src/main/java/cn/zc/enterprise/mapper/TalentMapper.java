package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.vo.TalentRecommendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人才推荐Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface TalentMapper {

    /**
     * 查询人才推荐列表
     */
    List<TalentRecommendVO> selectTalentRecommendList(@Param("enterpriseId") Long enterpriseId,
                                                      @Param("jobId") Long jobId,
                                                       @Param("requiredMajor") String requiredMajor,
                                                       @Param("requiredSkills") String requiredSkills,
                                                       @Param("requiredEducation") String requiredEducation,
                                                       @Param("requiredExperience") Integer requiredExperience,
                                                       @Param("campusId") Long campusId);

    /**
     * 面向首页的职位人才推荐（限制条数）
     */
    List<TalentRecommendVO> selectRecommendedTalentForJob(@Param("enterpriseId") Long enterpriseId,
                                                          @Param("jobId") Long jobId,
                                                          @Param("limit") Integer limit);
}











