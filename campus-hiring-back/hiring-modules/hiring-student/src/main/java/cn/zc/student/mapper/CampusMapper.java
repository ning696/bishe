package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Campus;
import cn.zc.student.domain.vo.CampusDetailVO;
import cn.zc.student.domain.vo.CampusListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 校园Mapper（学生端）
 *
 * @author
 */
@Mapper
public interface CampusMapper extends BaseMapper<Campus> {

    /**
     * 查询校园列表（仅展示启用状态）
     */
    List<CampusListVO> selectCampusList(@Param("keyword") String keyword);

    /**
     * 查询校园详情
     */
    CampusDetailVO selectCampusDetail(@Param("campusId") Long campusId);
}

