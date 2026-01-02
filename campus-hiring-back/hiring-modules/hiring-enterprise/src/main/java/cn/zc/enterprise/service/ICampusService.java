package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.vo.CampusJobVO;
import cn.zc.enterprise.domain.vo.CampusListVO;

import java.util.List;

/**
 * 校园服务接口
 * 
 * @author campus-hiring-system
 */
public interface ICampusService {

    /**
     * 校园列表查询
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, String campusName);

    /**
     * 职位校园关联查询
     */
    R<List<CampusJobVO>> getCampusJobList(Long jobId);
}

