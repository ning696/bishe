package cn.zc.admin.controller.enterprise;

import cn.zc.admin.domain.dto.EnterpriseCertificationAuditDTO;
import cn.zc.admin.service.IEnterpriseCertificationService;
import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业认证控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "企业认证管理", description = "企业认证审核相关接口")
@RestController
@RequestMapping("/admin/enterprise/certification")
public class EnterpriseCertificationController extends BaseController {

    @Autowired
    private IEnterpriseCertificationService certificationService;

    /**
     * 企业认证列表查询
     */
    @Operation(summary = "企业认证列表查询", description = "查询企业认证申请列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer certificationStatus,
                              @RequestParam(required = false) String enterpriseName,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return certificationService.getCertificationList(certificationStatus, enterpriseName, pageNum, pageSize);
    }

    /**
     * 企业认证审核
     */
    @Operation(summary = "企业认证审核", description = "审核企业认证申请")
    @PutMapping("/audit")
    public R<Void> audit(@RequestBody EnterpriseCertificationAuditDTO dto) {
        return certificationService.auditCertification(dto);
    }
}

