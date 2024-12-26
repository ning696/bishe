package com.gxyan.service;

import java.util.List;

import com.gxyan.common.ServerResponse;
import com.gxyan.pojo.Promotion;
import com.gxyan.vo.StoreQuery;

/**
 * 宣传Service接口
 *
 * @author ruoyi
 * @date 2024-11-14
 */
public interface IPromotionService
{
    /**
     * 查询宣传
     *
     * @param id 宣传主键
     * @return 宣传
     */
    public Promotion selectPromotionById(Long id);

    /**
     * 查询宣传列表
     *
     * @param promotion 宣传
     * @return 宣传集合
     */
    public List<Promotion> selectPromotionList(Promotion promotion);

    /**
     * 新增宣传
     *
     * @param promotion 宣传
     * @return 结果
     */
    public int insertPromotion(Promotion promotion);

    /**
     * 修改宣传
     *
     * @param promotion 宣传
     * @return 结果
     */
    public int updatePromotion(Promotion promotion);

    /**
     * 批量删除宣传
     *
     * @param ids 需要删除的宣传主键集合
     * @return 结果
     */
    public int deletePromotionByIds(Long[] ids);

    /**
     * 删除宣传信息
     *
     * @param id 宣传主键
     * @return 结果
     */
    public int deletePromotionById(Long id);
    ServerResponse getList(StoreQuery storeQuery);

    ServerResponse updatepromotion(Long aLong, String promotionText);
}
