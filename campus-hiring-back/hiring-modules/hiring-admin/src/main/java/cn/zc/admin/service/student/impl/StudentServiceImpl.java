package cn.zc.admin.service.student.impl;

import cn.zc.admin.domain.dto.StudentStatusUpdateDTO;
import cn.zc.admin.domain.entity.Student;
import cn.zc.admin.domain.vo.StudentDetailVO;
import cn.zc.admin.domain.vo.StudentListVO;
import cn.zc.admin.mapper.StudentMapper;
import cn.zc.admin.service.student.IStudentService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生管理服务实现
 *
 * @author
 */
@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public TableDataInfo queryStudentList(Integer status, String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentListVO> list = studentMapper.selectStudentList(status, keyword);
        PageInfo<StudentListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<StudentDetailVO> getStudentDetail(Long studentId) {
        StudentDetailVO detailVO = studentMapper.selectStudentDetail(studentId);
        if (detailVO == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return R.ok(detailVO);
    }

    @Override
    public R<Void> updateStudentStatus(StudentStatusUpdateDTO dto) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getId, dto.getStudentId())
                .set(Student::getStatus, dto.getStatus());
        int rows = studentMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

