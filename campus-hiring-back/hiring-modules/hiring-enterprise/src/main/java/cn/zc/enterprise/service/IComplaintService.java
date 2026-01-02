package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.ComplaintAddDTO;

/**
 * 投诉服务接口
 * 
 * @author campus-hiring-system
 */
public interface IComplaintService {

    /**
     * 提交投诉
     */
    R<Long> add(ComplaintAddDTO dto, Long enterpriseId);

    /**
     * 投诉查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer handleStatus, Long enterpriseId);
}

