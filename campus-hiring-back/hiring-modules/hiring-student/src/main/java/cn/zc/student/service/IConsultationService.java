package cn.zc.student.service;

import cn.zc.student.domain.dto.ConsultationAddDTO;
import cn.zc.student.domain.vo.ConsultationListVO;
import cn.zc.common.core.domain.R;

import java.util.List;

/**
 * 咨询服务接口
 * 
 * @author campus-hiring-system
 */
public interface IConsultationService {

    /**
     * 咨询列表查询
     */
    R<List<ConsultationListVO>> list(Long studentId);

    /**
     * 发起咨询
     */
    R<Void> add(ConsultationAddDTO dto, Long studentId);
}

