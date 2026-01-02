package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.Complaint;
import cn.zc.enterprise.domain.vo.ComplaintListVO;
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
    List<ComplaintListVO> selectComplaintList(@Param("enterpriseId") Long enterpriseId,
                                              @Param("handleStatus") Integer handleStatus);

    /**
     * 检查学生是否存在
     */
    int countStudentById(@Param("studentId") Long studentId);

    /**
     * 检查职位是否存在且属于企业
     */
    int countJobByIdAndEnterprise(@Param("jobId") Long jobId,
                                  @Param("enterpriseId") Long enterpriseId);
}

