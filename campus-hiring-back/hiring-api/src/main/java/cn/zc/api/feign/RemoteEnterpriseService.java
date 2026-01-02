package cn.zc.api.feign;

import cn.zc.api.domain.dto.ConsultationDTO;
import cn.zc.api.domain.vo.EnterpriseInfoVO;
import cn.zc.api.domain.vo.JobDetailVO;
import cn.zc.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 企业服务调用接口
 * 
 * @author campus-hiring-system
 */
@FeignClient(contextId = "RemoteEnterpriseService", value = "hiring-enterprise", url = "http://localhost:8083")
public interface RemoteEnterpriseService {

    /**
     * 获取企业信息
     * 
     * @param enterpriseId 企业ID
     * @return 企业信息
     */
    @GetMapping("/enterprise/enterprise/info/{enterpriseId}")
    R<EnterpriseInfoVO> getEnterpriseInfo(@PathVariable("enterpriseId") Long enterpriseId);

    /**
     * 获取职位详情
     * 
     * @param jobId 职位ID
     * @return 职位详情
     */
    @GetMapping("/enterprise/job/detail/{jobId}")
    R<JobDetailVO> getJobDetail(@PathVariable("jobId") Long jobId);

    /**
     * 提交咨询
     * 
     * @param dto 咨询DTO
     * @return 操作结果
     */
    @PostMapping("/enterprise/consultation/reply")
    R<Void> submitConsultation(@RequestBody ConsultationDTO dto);

    /**
     * 获取企业信息（用于聊天）
     * 
     * @param enterpriseId 企业ID
     * @return 企业信息
     */
    @GetMapping("/enterprise/enterprise/info/{enterpriseId}")
    R<EnterpriseInfoVO> getEnterpriseInfoForChat(@PathVariable("enterpriseId") Long enterpriseId);
}

