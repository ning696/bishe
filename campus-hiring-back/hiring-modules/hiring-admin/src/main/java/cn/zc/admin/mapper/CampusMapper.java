package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Campus;
import cn.zc.admin.domain.vo.CampusDetailVO;
import cn.zc.admin.domain.vo.CampusListVO;
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
    List<CampusListVO> selectCampusList(@Param("status") Integer status,
                                         @Param("keyword") String keyword);

    /**
     * 查询校园详情
     */
    CampusDetailVO selectCampusDetail(@Param("campusId") Long campusId);
}

