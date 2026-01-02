package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.ComplaintAddDTO;
import cn.zc.student.domain.dto.ComplaintQueryDTO;
import cn.zc.student.domain.entity.Complaint;
import cn.zc.student.domain.vo.ComplaintListVO;
import cn.zc.student.mapper.ComplaintMapper;
import cn.zc.student.service.IComplaintService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 投诉服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ComplaintServiceImpl implements IComplaintService {

    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    @Transactional
    public R<Long> add(ComplaintAddDTO dto, Long studentId) {
        if (dto == null || dto.getEnterpriseId() == null
                || !StringUtils.hasText(dto.getTitle())
                || !StringUtils.hasText(dto.getContent())) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        int enterpriseCount = complaintMapper.countEnterpriseById(dto.getEnterpriseId());
        if (enterpriseCount <= 0) {
            return R.fail("被投诉企业不存在", ResultCode.FAILED_NOT_EXISTS);
        }

        if (dto.getJobId() != null) {
            int jobCount = complaintMapper.countJobByIdAndEnterprise(dto.getJobId(), dto.getEnterpriseId());
            if (jobCount <= 0) {
                return R.fail(ResultCode.FAILED_JOB_NOT_EXISTS);
            }
        }

        Complaint complaint = new Complaint();
        complaint.setComplaintType(1); // 投诉类型：1-学生投诉企业
        complaint.setComplainerId(studentId);
        complaint.setComplainerType(1); // 投诉人类型：1-学生
        complaint.setComplainedId(dto.getEnterpriseId());
        complaint.setComplainedType(2); // 被投诉人类型：2-企业
        complaint.setJobId(dto.getJobId());
        complaint.setTitle(dto.getTitle());
        complaint.setContent(dto.getContent());
        complaint.setAttachment(dto.getAttachment());
        complaint.setHandleStatus(0); // 待处理
        complaint.setCreateBy(studentId);
        complaint.setUpdateBy(studentId);

        int rows = complaintMapper.insert(complaint);
        return rows > 0 ? R.ok(complaint.getId()) : R.fail();
    }

    @Override
    public R<TableDataInfo> list(ComplaintQueryDTO query, Long studentId) {
        ComplaintQueryDTO condition = query != null ? query : new ComplaintQueryDTO();
        int pageNum = condition.getPageNum() != null ? condition.getPageNum() : 1;
        int pageSize = condition.getPageSize() != null ? condition.getPageSize() : 10;

        PageHelper.startPage(pageNum, pageSize);
        List<ComplaintListVO> list = complaintMapper.selectComplaintList(studentId, condition.getHandleStatus());
        PageInfo<ComplaintListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }
}

