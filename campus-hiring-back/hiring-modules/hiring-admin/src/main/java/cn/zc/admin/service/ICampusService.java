package cn.zc.admin.service;

import cn.zc.admin.domain.dto.CampusAddDTO;
import cn.zc.admin.domain.dto.CampusUpdateDTO;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;

/**
 * 校园管理服务接口
 * 
 * @author campus-hiring-system
 */
public interface ICampusService {

    /**
     * 校园列表查询
     */
    TableDataInfo getCampusList(Integer status, String keyword, Integer pageNum, Integer pageSize);

    /**
     * 校园详情查询
     */
    R<Object> getCampusDetail(Long campusId);

    /**
     * 新增校园
     */
    R<Void> addCampus(CampusAddDTO dto);

    /**
     * 更新校园
     */
    R<Void> updateCampus(CampusUpdateDTO dto);

    /**
     * 删除校园
     */
    R<Void> deleteCampus(Long campusId);
}

