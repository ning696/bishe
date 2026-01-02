package cn.zc.admin.service;

import cn.zc.admin.domain.dto.ComplaintHandleDTO;
import cn.zc.admin.domain.vo.ComplaintDetailVO;
import cn.zc.admin.domain.vo.ComplaintListVO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 投诉管理服务接口
 * 
 * @author campus-hiring-system
 */
public interface IComplaintService {

    /**
     * 投诉列表查询
     */
    TableDataInfo getComplaintList(Integer handleStatus, Integer complaintType, Integer pageNum, Integer pageSize);

    /**
     * 投诉详情查询
     */
    R<ComplaintDetailVO> getComplaintDetail(Long complaintId);

    /**
     * 处理投诉
     */
    R<Void> handleComplaint(ComplaintHandleDTO dto, Long adminId);
}

