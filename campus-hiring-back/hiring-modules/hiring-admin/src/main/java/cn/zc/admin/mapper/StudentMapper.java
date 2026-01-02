package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Student;
import cn.zc.admin.domain.vo.StudentDetailVO;
import cn.zc.admin.domain.vo.StudentListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 查询学生列表
     */
    List<StudentListVO> selectStudentList(@Param("status") Integer status,
                                          @Param("keyword") String keyword);

    /**
     * 查询学生详情
     */
    StudentDetailVO selectStudentDetail(@Param("studentId") Long studentId);
}

