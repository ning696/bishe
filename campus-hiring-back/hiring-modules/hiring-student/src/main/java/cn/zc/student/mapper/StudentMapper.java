package cn.zc.student.mapper;

import cn.zc.student.domain.entity.Student;
import cn.zc.student.domain.vo.StudentDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学生Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 查询学生详情
     */
    StudentDetailVO selectStudentDetail(@Param("studentId") Long studentId);
}

