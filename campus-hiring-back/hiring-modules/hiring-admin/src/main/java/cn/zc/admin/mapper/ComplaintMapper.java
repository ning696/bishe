package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Complaint;
import cn.zc.admin.domain.vo.ComplaintDetailVO;
import cn.zc.admin.domain.vo.ComplaintListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 投诉Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {

    /**
     * 查询投诉列表
     */
    List<ComplaintListVO> selectComplaintList(@Param("handleStatus") Integer handleStatus,
                                                @Param("complaintType") Integer complaintType);

    /**
     * 查询投诉详情
     */
    ComplaintDetailVO selectComplaintDetail(@Param("complaintId") Long complaintId);
}

