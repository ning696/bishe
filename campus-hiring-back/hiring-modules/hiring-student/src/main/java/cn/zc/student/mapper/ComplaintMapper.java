package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Complaint;
import cn.zc.student.domain.vo.ComplaintListVO;
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
    List<ComplaintListVO> selectComplaintList(@Param("studentId") Long studentId,
                                              @Param("handleStatus") Integer handleStatus);

    /**
     * 检查企业是否存在
     */
    int countEnterpriseById(@Param("enterpriseId") Long enterpriseId);

    /**
     * 检查职位是否存在且属于指定企业
     */
    int countJobByIdAndEnterprise(@Param("jobId") Long jobId,
                                  @Param("enterpriseId") Long enterpriseId);
}

