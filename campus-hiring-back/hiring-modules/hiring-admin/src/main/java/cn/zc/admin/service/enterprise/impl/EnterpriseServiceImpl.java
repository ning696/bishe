package cn.zc.admin.service.enterprise.impl;

import cn.zc.admin.domain.dto.EnterpriseStatusUpdateDTO;
import cn.zc.admin.domain.entity.Enterprise;
import cn.zc.admin.domain.vo.EnterpriseDetailVO;
import cn.zc.admin.domain.vo.EnterpriseListVO;
import cn.zc.admin.mapper.EnterpriseMapper;
import cn.zc.admin.service.enterprise.IEnterpriseService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业管理服务实现
 *
 * @author
 */
@Service
public class EnterpriseServiceImpl implements IEnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Override
    public TableDataInfo queryEnterpriseList(Integer status, String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EnterpriseListVO> list = enterpriseMapper.selectEnterpriseList(status, keyword);
        PageInfo<EnterpriseListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<EnterpriseDetailVO> getEnterpriseDetail(Long enterpriseId) {
        EnterpriseDetailVO detailVO = enterpriseMapper.selectEnterpriseDetail(enterpriseId);
        if (detailVO == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return R.ok(detailVO);
    }

    @Override
    public R<Void> updateEnterpriseStatus(EnterpriseStatusUpdateDTO dto) {
        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, dto.getEnterpriseId())
                .set(Enterprise::getStatus, dto.getStatus());
        int rows = enterpriseMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }
}

