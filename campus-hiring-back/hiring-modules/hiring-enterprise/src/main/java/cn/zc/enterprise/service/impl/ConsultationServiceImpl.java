package cn.zc.enterprise.service.impl;

import cn.zc.api.domain.dto.ConsultationDTO;
import cn.zc.api.feign.RemoteStudentService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.ConsultationReplyDTO;
import cn.zc.enterprise.domain.entity.Consultation;
import cn.zc.enterprise.domain.vo.ConsultationListVO;
import cn.zc.enterprise.mapper.ConsultationMapper;
import cn.zc.enterprise.service.IConsultationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 咨询服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ConsultationServiceImpl implements IConsultationService {

    @Autowired
    private ConsultationMapper consultationMapper;

    @Autowired
    private RemoteStudentService remoteStudentService;

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer status,
                                Long studentId, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ConsultationListVO> list = consultationMapper.selectConsultationList(enterpriseId, status, studentId);
        PageInfo<ConsultationListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<Void> reply(ConsultationReplyDTO dto, Long enterpriseId) {
        Consultation consultation = consultationMapper.selectById(dto.getConsultationId());
        if (consultation == null || !consultation.getEnterpriseId().equals(enterpriseId)) {
            return R.fail();
        }

        // 更新本地咨询记录
        consultation.setReplyContent(dto.getReplyContent());
        consultation.setReplyTime(LocalDateTime.now());
        consultation.setStatus(1); // 已回复
        consultationMapper.updateById(consultation);

        // 通过 Feign 调用学生服务更新咨询记录
        ConsultationDTO consultationDTO = new ConsultationDTO();
        consultationDTO.setId(dto.getConsultationId());
        consultationDTO.setStudentId(consultation.getStudentId());
        consultationDTO.setEnterpriseId(enterpriseId);
        consultationDTO.setJobId(consultation.getJobId());
        consultationDTO.setReplyContent(dto.getReplyContent());
        consultationDTO.setStatus(1);
        
        R<Void> result = remoteStudentService.updateConsultation(consultationDTO);
        return result;
    }
}











