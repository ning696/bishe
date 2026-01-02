package cn.zc.student.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.student.domain.vo.ApplicationItemVO;
import cn.zc.student.mapper.JobApplicationMapper;
import cn.zc.student.service.IStudentPersonalApplicationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentPersonalApplicationServiceImpl implements IStudentPersonalApplicationService {

	@Autowired
	private JobApplicationMapper jobApplicationMapper;

	@Override
	public R<TableDataInfo> page(Long studentId, Integer pageNum, Integer pageSize, Integer status) {
		PageHelper.startPage(pageNum, pageSize);
		List<ApplicationItemVO> list = jobApplicationMapper.selectPersonalApplicationPage(studentId, status);
		PageInfo<ApplicationItemVO> pageInfo = new PageInfo<>(list);
		TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
		return R.ok(dataInfo);
	}
}


