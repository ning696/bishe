package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.ConsultationAddDTO;
import cn.zc.student.domain.entity.Consultation;
import cn.zc.student.domain.vo.ConsultationListVO;
import cn.zc.student.mapper.ConsultationMapper;
import cn.zc.student.service.IConsultationService;
import cn.zc.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public R<List<ConsultationListVO>> list(Long studentId) {
        List<ConsultationListVO> list = consultationMapper.selectConsultationList(studentId);
        return R.ok(list);
    }

    @Override
    @Transactional
    public R<Void> add(ConsultationAddDTO dto, Long studentId) {
        Consultation consultation = new Consultation();
        consultation.setStudentId(studentId);
        consultation.setEnterpriseId(dto.getEnterpriseId());
        consultation.setJobId(dto.getJobId());
        consultation.setConsultationType(dto.getConsultationType());
        consultation.setTitle(dto.getTitle());
        consultation.setContent(dto.getContent());
        consultation.setStatus(0); // 待回复

        int rows = consultationMapper.insert(consultation);
        return rows > 0 ? R.ok() : R.fail();
    }
}

