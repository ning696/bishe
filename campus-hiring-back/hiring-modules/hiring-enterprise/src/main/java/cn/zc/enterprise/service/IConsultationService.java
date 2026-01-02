package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.ConsultationReplyDTO;

/**
 * 咨询服务接口
 * 
 * @author campus-hiring-system
 */
public interface IConsultationService {

    /**
     * 咨询列表查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer status,
                         Long studentId, Long enterpriseId);

    /**
     * 回复咨询
     */
    R<Void> reply(ConsultationReplyDTO dto, Long enterpriseId);
}











