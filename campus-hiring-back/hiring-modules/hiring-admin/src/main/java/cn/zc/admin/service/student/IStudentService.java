package cn.zc.admin.service.student;

import cn.zc.admin.domain.dto.StudentStatusUpdateDTO;
import cn.zc.admin.domain.vo.StudentDetailVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 学生管理服务接口
 *
 * @author
 */
public interface IStudentService {

    /**
     * 分页查询学生列表
     */
    TableDataInfo queryStudentList(Integer status, String keyword, Integer pageNum, Integer pageSize);

    /**
     * 查询学生详情
     */
    R<StudentDetailVO> getStudentDetail(Long studentId);

    /**
     * 更新学生状态
     */
    R<Void> updateStudentStatus(StudentStatusUpdateDTO dto);
}

