package cn.zc.admin.service.impl;

import cn.zc.admin.domain.dto.CampusAddDTO;
import cn.zc.admin.domain.dto.CampusUpdateDTO;
import cn.zc.admin.domain.entity.Campus;
import cn.zc.admin.domain.vo.CampusDetailVO;
import cn.zc.admin.domain.vo.CampusListVO;
import cn.zc.admin.mapper.CampusMapper;
import cn.zc.admin.service.ICampusService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 校园管理服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class CampusServiceImpl implements ICampusService {

    @Autowired
    private CampusMapper campusMapper;

    @Override
    public TableDataInfo getCampusList(Integer status, String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CampusListVO> list = campusMapper.selectCampusList(status, keyword);
        PageInfo<CampusListVO> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    @Override
    public R<Object> getCampusDetail(Long campusId) {
        CampusDetailVO detail = campusMapper.selectCampusDetail(campusId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<Void> addCampus(CampusAddDTO dto) {
        // 检查校园名称是否已存在
        LambdaQueryWrapper<Campus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Campus::getCampusName, dto.getCampusName());
        Campus existCampus = campusMapper.selectOne(queryWrapper);
        if (existCampus != null) {
            return R.fail(ResultCode.FAILED_ALREADY_EXISTS);
        }

        // 创建新校园
        Campus campus = new Campus();
        BeanUtils.copyProperties(dto, campus);
        // 如果状态为空，默认设置为启用
        if (campus.getStatus() == null) {
            campus.setStatus(1);
        }
        int rows = campusMapper.insert(campus);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> updateCampus(CampusUpdateDTO dto) {
        // 检查校园是否存在
        Campus campus = campusMapper.selectById(dto.getId());
        if (campus == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 如果修改了校园名称，检查新名称是否已存在
        if (StringUtils.hasText(dto.getCampusName()) && !dto.getCampusName().equals(campus.getCampusName())) {
            LambdaQueryWrapper<Campus> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Campus::getCampusName, dto.getCampusName())
                    .ne(Campus::getId, dto.getId());
            Campus existCampus = campusMapper.selectOne(queryWrapper);
            if (existCampus != null) {
                return R.fail(ResultCode.FAILED_ALREADY_EXISTS);
            }
        }

        // 更新校园信息
        BeanUtils.copyProperties(dto, campus);
        int rows = campusMapper.updateById(campus);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> deleteCampus(Long campusId) {
        // 检查校园是否存在
        Campus campus = campusMapper.selectById(campusId);
        if (campus == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 删除校园
        int rows = campusMapper.deleteById(campusId);
        return rows > 0 ? R.ok() : R.fail();
    }
}

