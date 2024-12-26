package com.gxyan.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.gxyan.common.ServerResponse;
import com.gxyan.pojo.PromotionVO;
import com.gxyan.vo.ListVo;
import com.gxyan.vo.StoreList;
import com.gxyan.vo.StoreQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gxyan.dao.PromotionMapper;
import com.gxyan.pojo.Promotion;
import com.gxyan.service.IPromotionService;

/**
 * 宣传Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-14
 */
@Service
public class PromotionServiceImpl implements IPromotionService
{
    @Autowired
    private PromotionMapper promotionMapper;

    /**
     * 查询宣传
     *
     * @param id 宣传主键
     * @return 宣传
     */
    @Override
    public Promotion selectPromotionById(Long id)
    {
        return promotionMapper.selectPromotionById(id);
    }

    /**
     * 查询宣传列表
     *
     * @param promotion 宣传
     * @return 宣传
     */
    @Override
    public List<Promotion> selectPromotionList(Promotion promotion)
    {
        return promotionMapper.selectPromotionList(promotion);
    }

    /**
     * 新增宣传
     *
     * @param promotion 宣传
     * @return 结果
     */
    @Override
    public int insertPromotion(Promotion promotion)
    {
        return promotionMapper.insertPromotion(promotion);
    }

    /**
     * 修改宣传
     *
     * @param promotion 宣传
     * @return 结果
     */
    @Override
    public int updatePromotion(Promotion promotion)
    {
        return promotionMapper.updatePromotion(promotion);
    }

    /**
     * 批量删除宣传
     *
     * @param ids 需要删除的宣传主键
     * @return 结果
     */
    @Override
    public int deletePromotionByIds(Long[] ids)
    {
        return promotionMapper.deletePromotionByIds(ids);
    }

    /**
     * 删除宣传信息
     *
     * @param id 宣传主键
     * @return 结果
     */
    @Override
    public int deletePromotionById(Long id)
    {
        return promotionMapper.deletePromotionById(id);
    }
    @Override
    public ServerResponse getList(StoreQuery storeQuery) {
        List<PromotionVO> list = PageHelper.startPage(storeQuery.getPage(), storeQuery.getLimit()).doSelectPage(()-> promotionMapper.selectSelective(storeQuery));
        if (list != null) {
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(PageHelper.count(()->promotionMapper.selectSelective(storeQuery)));
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取库存列表失败");
    }

    @Override
    public ServerResponse updatepromotion(Long aLong, String promotionText) {
        int updatepromotion = promotionMapper.updatepromotion(aLong, promotionText);
        return updatepromotion>0?ServerResponse.createBySuccess("修改成功"):ServerResponse.createByErrorMessage("修改失败");
    }
}
