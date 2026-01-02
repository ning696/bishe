package cn.zc.student.service;

import cn.zc.student.domain.dto.ComplaintAddDTO;
import cn.zc.student.domain.dto.ComplaintQueryDTO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 投诉服务接口
 * 
 * @author campus-hiring-system
 */
public interface IComplaintService {

    /**
     * 提交投诉
     */
    R<Long> add(ComplaintAddDTO dto, Long studentId);

    /**
     * 投诉查询
     */
    R<TableDataInfo> list(ComplaintQueryDTO query, Long studentId);
}

