package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.vo.CampusJobVO;
import cn.zc.enterprise.domain.vo.CampusListVO;
import cn.zc.enterprise.mapper.CampusMapper;
import cn.zc.enterprise.service.ICampusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 校园服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class CampusServiceImpl implements ICampusService {

    @Autowired
    private CampusMapper campusMapper;

    @Override
    public R<TableDataInfo> list(Integer pageNum, Integer pageSize, String campusName) {
        PageHelper.startPage(pageNum, pageSize);
        List<CampusListVO> list = campusMapper.selectCampusList(campusName);
        PageInfo<CampusListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<List<CampusJobVO>> getCampusJobList(Long jobId) {
        List<CampusJobVO> list = campusMapper.selectCampusJobList(jobId);
        return R.ok(list);
    }
}

