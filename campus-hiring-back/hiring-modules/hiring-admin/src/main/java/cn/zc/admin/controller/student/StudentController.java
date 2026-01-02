package cn.zc.admin.controller.student;

import cn.zc.admin.domain.dto.StudentStatusUpdateDTO;
import cn.zc.admin.domain.vo.StudentDetailVO;
import cn.zc.admin.service.student.IStudentService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学生管理控制器
 *
 * @author
 */
@Tag(name = "学生管理（管理员）", description = "管理员端学生管理接口")
@RestController
@RequestMapping("/admin/student")
public class StudentController extends BaseController {

    @Autowired
    private IStudentService studentService;

    @Operation(summary = "学生列表", description = "分页查询学生列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer status,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return studentService.queryStudentList(status, keyword, pageNum, pageSize);
    }

    @Operation(summary = "学生详情", description = "查询指定学生详情")
    @GetMapping("/detail")
    public R<StudentDetailVO> detail(@RequestParam Long studentId) {
        return studentService.getStudentDetail(studentId);
    }

    @Operation(summary = "更新学生状态", description = "启用/禁用学生账号")
    @PutMapping("/updateStatus")
    public R<Void> updateStatus(@RequestBody StudentStatusUpdateDTO dto) {
        return studentService.updateStudentStatus(dto);
    }
}

