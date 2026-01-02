package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.Enterprise;
import cn.zc.enterprise.domain.vo.EnterpriseDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    /**
     * 查询企业详情
     */
    EnterpriseDetailVO selectEnterpriseDetail(@Param("enterpriseId") Long enterpriseId);
}

