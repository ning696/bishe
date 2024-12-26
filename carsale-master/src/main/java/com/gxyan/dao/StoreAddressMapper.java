package com.gxyan.dao;

import java.util.List;
import com.gxyan.domain.StoreAddress;

/**
 * 门店Mapper接口
 *
 * @author cool
 * @date 2024-11-09
 */
public interface StoreAddressMapper
{
    /**
     * 查询门店
     *
     * @param id 门店主键
     * @return 门店
     */
    public StoreAddress selectStoreAddressById(Long id);

    /**
     * 查询门店列表
     *
     * @param storeAddress 门店
     * @return 门店集合
     */
    public List<StoreAddress> selectStoreAddressList(StoreAddress storeAddress);

    /**
     * 新增门店
     *
     * @param storeAddress 门店
     * @return 结果
     */
    public int insertStoreAddress(StoreAddress storeAddress);

    /**
     * 修改门店
     *
     * @param storeAddress 门店
     * @return 结果
     */
    public int updateStoreAddress(StoreAddress storeAddress);

    /**
     * 删除门店
     *
     * @param id 门店主键
     * @return 结果
     */
    public int deleteStoreAddressById(Long id);

    /**
     * 批量删除门店
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreAddressByIds(Long[] ids);
}
