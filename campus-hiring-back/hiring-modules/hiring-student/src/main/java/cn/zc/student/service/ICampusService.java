package cn.zc.student.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.student.domain.vo.CampusDetailVO;

/**
 * 学生端校园服务接口
 *
 * @author
 */
public interface ICampusService {

    /**
     * 查询校园列表
     */
    R<TableDataInfo> list(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 查询校园详情
     */
    R<CampusDetailVO> detail(Long campusId);
}

