package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.ComplaintHandleDTO;
import cn.zc.admin.domain.entity.Complaint;
import cn.zc.admin.domain.vo.ComplaintDetailVO;
import cn.zc.admin.domain.vo.ComplaintListVO;
import cn.zc.admin.mapper.ComplaintMapper;
import cn.zc.admin.service.IComplaintService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 投诉管理服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ComplaintServiceImpl implements IComplaintService {

    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    public TableDataInfo getComplaintList(Integer handleStatus, Integer complaintType, Integer pageNum, Integer pageSize) {
        int currentPage = (pageNum != null && pageNum > 0) ? pageNum : 1;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;

        PageHelper.startPage(currentPage, size);
        List<ComplaintListVO> list = complaintMapper.selectComplaintList(handleStatus, complaintType);
        PageInfo<ComplaintListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<ComplaintDetailVO> getComplaintDetail(Long complaintId) {
        ComplaintDetailVO detail = complaintMapper.selectComplaintDetail(complaintId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<Void> handleComplaint(ComplaintHandleDTO dto, Long adminId) {
        if (dto == null || dto.getComplaintId() == null || dto.getHandleStatus() == null) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        if (dto.getHandleStatus() < 1 || dto.getHandleStatus() > 3) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        Complaint complaint = complaintMapper.selectById(dto.getComplaintId());
        if (complaint == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        LambdaUpdateWrapper<Complaint> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Complaint::getId, dto.getComplaintId())
                .set(Complaint::getHandleStatus, dto.getHandleStatus())
                .set(Complaint::getHandleResult, dto.getHandleResult())
                .set(Complaint::getHandleRemark, dto.getHandleRemark())
                .set(Complaint::getHandleTime, LocalDateTime.now())
                .set(Complaint::getHandleBy, adminId);

        int rows = complaintMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

