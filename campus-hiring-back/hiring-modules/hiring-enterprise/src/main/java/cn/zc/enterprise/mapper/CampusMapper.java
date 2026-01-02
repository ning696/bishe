package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.Campus;
import cn.zc.enterprise.domain.vo.CampusJobVO;
import cn.zc.enterprise.domain.vo.CampusListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 校园Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface CampusMapper extends BaseMapper<Campus> {

    /**
     * 查询校园列表
     */
    List<CampusListVO> selectCampusList(@Param("campusName") String campusName);

    /**
     * 查询职位校园关联列表
     */
    List<CampusJobVO> selectCampusJobList(@Param("jobId") Long jobId);
}

