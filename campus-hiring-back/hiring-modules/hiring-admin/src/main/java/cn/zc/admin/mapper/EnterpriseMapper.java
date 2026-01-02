package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Enterprise;
import cn.zc.admin.domain.vo.EnterpriseCertificationListVO;
import cn.zc.admin.domain.vo.EnterpriseDetailVO;
import cn.zc.admin.domain.vo.EnterpriseListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    /**
     * 查询企业列表
     */
    List<EnterpriseListVO> selectEnterpriseList(@Param("status") Integer status,
                                                @Param("keyword") String keyword);

    /**
     * 查询企业详情
     */
    EnterpriseDetailVO selectEnterpriseDetail(@Param("enterpriseId") Long enterpriseId);

    /**
     * 查询企业认证列表
     */
    List<EnterpriseCertificationListVO> selectCertificationList(@Param("certificationStatus") Integer certificationStatus,
                                                                  @Param("enterpriseName") String enterpriseName);
}

