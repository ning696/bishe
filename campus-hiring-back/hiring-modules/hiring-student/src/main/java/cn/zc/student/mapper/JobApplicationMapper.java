package cn.zc.student.mapper;

import cn.zc.student.domain.entity.JobApplication;
import cn.zc.student.domain.vo.ApplicationItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 职位申请Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface JobApplicationMapper extends BaseMapper<JobApplication> {
	/**
	 * 学生个人中心-职位申请分页
	 */
	List<ApplicationItemVO> selectPersonalApplicationPage(Long studentId, Integer status);
}

