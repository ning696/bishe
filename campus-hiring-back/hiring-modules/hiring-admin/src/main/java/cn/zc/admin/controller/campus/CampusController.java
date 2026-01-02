package cn.zc.admin.controller.campus;

import cn.zc.admin.domain.dto.CampusAddDTO;
import cn.zc.admin.domain.dto.CampusUpdateDTO;
import cn.zc.admin.service.ICampusService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 校园管理控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "校园管理（管理员）", description = "管理员端校园管理相关接口")
@RestController
@RequestMapping("/admin/campus")
public class CampusController extends BaseController {

    @Autowired
    private ICampusService campusService;

    /**
     * 校园列表查询
     */
    @Operation(summary = "校园列表查询", description = "分页查询校园列表，支持状态和关键词筛选")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer status,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return campusService.getCampusList(status, keyword, pageNum, pageSize);
    }

    /**
     * 校园详情查询
     */
    @Operation(summary = "校园详情查询", description = "查询指定校园的详细信息")
    @GetMapping("/detail")
    public R<Object> detail(@RequestParam Long campusId) {
        return campusService.getCampusDetail(campusId);
    }

    /**
     * 新增校园
     */
    @Operation(summary = "新增校园", description = "创建新的校园记录")
    @PostMapping("/add")
    public R<Void> add(@RequestBody CampusAddDTO dto) {
        return campusService.addCampus(dto);
    }

    /**
     * 更新校园
     */
    @Operation(summary = "更新校园", description = "更新指定校园的信息")
    @PutMapping("/update")
    public R<Void> update(@RequestBody CampusUpdateDTO dto) {
        return campusService.updateCampus(dto);
    }

    /**
     * 删除校园
     */
    @Operation(summary = "删除校园", description = "删除指定的校园记录")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam Long campusId) {
        return campusService.deleteCampus(campusId);
    }
}

