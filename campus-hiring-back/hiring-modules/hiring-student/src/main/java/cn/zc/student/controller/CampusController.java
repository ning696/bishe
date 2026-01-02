package cn.zc.student.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.student.domain.vo.CampusDetailVO;
import cn.zc.student.service.ICampusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 校园查询控制器（学生端）
 *
 * @author
 */
@Tag(name = "校园信息（学生端）", description = "学生端校园信息查询接口")
@RestController
@RequestMapping("/student/campus")
public class CampusController extends BaseController {

    @Autowired
    private ICampusService campusService;

    /**
     * 校园列表查询
     */
    @Operation(summary = "校园列表查询", description = "分页查询启用中的校园信息")
    @GetMapping("/list")
    public R<TableDataInfo> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String keyword) {
        return campusService.list(pageNum, pageSize, keyword);
    }

    /**
     * 校园详情
     */
    @Operation(summary = "校园详情查询", description = "获取指定校园的详细信息")
    @GetMapping("/detail/{campusId}")
    public R<CampusDetailVO> detail(@PathVariable("campusId") Long campusId) {
        return campusService.detail(campusId);
    }
}

