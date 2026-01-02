package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Consultation;
import cn.zc.student.domain.vo.ConsultationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 咨询Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface ConsultationMapper extends BaseMapper<Consultation> {

    /**
     * 查询咨询列表
     */
    List<ConsultationListVO> selectConsultationList(@Param("studentId") Long studentId);
}

