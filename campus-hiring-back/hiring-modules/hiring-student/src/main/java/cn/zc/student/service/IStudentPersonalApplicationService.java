package cn.zc.student.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 学生个人中心-职位申请 服务接口
 */
public interface IStudentPersonalApplicationService {

	/**
	 * 分页查询学生的职位申请记录
	 */
	R<TableDataInfo> page(Long studentId, Integer pageNum, Integer pageSize, Integer status);
}


