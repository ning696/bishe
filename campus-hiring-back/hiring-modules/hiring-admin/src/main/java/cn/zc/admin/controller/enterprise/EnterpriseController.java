package cn.zc.admin.controller.enterprise;

import cn.zc.admin.domain.dto.EnterpriseStatusUpdateDTO;
import cn.zc.admin.domain.vo.EnterpriseDetailVO;
import cn.zc.admin.service.enterprise.IEnterpriseService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业管理控制器
 *
 * @author
 */
@Tag(name = "企业管理（管理员）", description = "管理员端企业管理接口")
@RestController
@RequestMapping("/admin/enterprise")
public class EnterpriseController extends BaseController {

    @Autowired
    private IEnterpriseService enterpriseService;

    @Operation(summary = "企业列表", description = "分页查询企业列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer status,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return enterpriseService.queryEnterpriseList(status, keyword, pageNum, pageSize);
    }

    @Operation(summary = "企业详情", description = "查询指定企业详情信息")
    @GetMapping("/detail")
    public R<EnterpriseDetailVO> detail(@RequestParam Long enterpriseId) {
        return enterpriseService.getEnterpriseDetail(enterpriseId);
    }

    @Operation(summary = "更新企业状态", description = "启用/禁用企业账号")
    @PutMapping("/updateStatus")
    public R<Void> updateStatus(@RequestBody EnterpriseStatusUpdateDTO dto) {
        return enterpriseService.updateEnterpriseStatus(dto);
    }
}

