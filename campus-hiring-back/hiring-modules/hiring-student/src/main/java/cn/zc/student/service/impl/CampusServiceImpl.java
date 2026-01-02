package cn.zc.student.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.student.domain.vo.CampusDetailVO;
import cn.zc.student.domain.vo.CampusListVO;
import cn.zc.student.mapper.CampusMapper;
import cn.zc.student.service.ICampusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生端校园服务实现类
 *
 * @author
 */
@Service
public class CampusServiceImpl implements ICampusService {

    @Autowired
    private CampusMapper campusMapper;

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<CampusListVO> list = campusMapper.selectCampusList(keyword);
        PageInfo<CampusListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo data = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(data);
    }

    @Override
    public R<CampusDetailVO> detail(Long campusId) {
        CampusDetailVO detail = campusMapper.selectCampusDetail(campusId);
        if (detail == null || detail.getStatus() == null || detail.getStatus() != 1) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(detail);
    }
}

