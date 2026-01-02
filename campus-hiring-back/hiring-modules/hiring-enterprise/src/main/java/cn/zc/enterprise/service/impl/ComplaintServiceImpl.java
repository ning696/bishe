package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.ComplaintAddDTO;
import cn.zc.enterprise.domain.entity.Complaint;
import cn.zc.enterprise.domain.vo.ComplaintListVO;
import cn.zc.enterprise.mapper.ComplaintMapper;
import cn.zc.enterprise.service.IComplaintService;
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
    @Transactional(rollbackFor = Exception.class)
    public R<Long> add(ComplaintAddDTO dto, Long enterpriseId) {
        if (dto == null || dto.getStudentId() == null
                || !StringUtils.hasText(dto.getTitle())
                || !StringUtils.hasText(dto.getContent())) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        int studentCount = complaintMapper.countStudentById(dto.getStudentId());
        if (studentCount <= 0) {
            return R.fail("被投诉学生不存在", ResultCode.FAILED_NOT_EXISTS);
        }

        if (dto.getJobId() != null) {
            int jobCount = complaintMapper.countJobByIdAndEnterprise(dto.getJobId(), enterpriseId);
            if (jobCount <= 0) {
                return R.fail(ResultCode.FAILED_JOB_NOT_EXISTS);
            }
        }

        Complaint complaint = new Complaint();
        complaint.setComplaintType(2); // 投诉类型：2-企业投诉学生
        complaint.setComplainerId(enterpriseId);
        complaint.setComplainerType(2); // 投诉人类型：2-企业
        complaint.setComplainedId(dto.getStudentId());
        complaint.setComplainedType(1); // 被投诉人类型：1-学生
        complaint.setJobId(dto.getJobId());
        complaint.setTitle(dto.getTitle());
        complaint.setContent(dto.getContent());
        complaint.setAttachment(dto.getAttachment());
        complaint.setHandleStatus(0); // 待处理
        complaint.setCreateBy(enterpriseId);
        complaint.setUpdateBy(enterpriseId);

        int rows = complaintMapper.insert(complaint);
        return rows > 0 ? R.ok(complaint.getId()) : R.fail();
    }

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, Integer handleStatus, Long enterpriseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ComplaintListVO> list = complaintMapper.selectComplaintList(enterpriseId, handleStatus);
        PageInfo<ComplaintListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }
}

