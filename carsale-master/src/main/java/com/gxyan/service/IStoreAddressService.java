package com.gxyan.service;

import java.util.List;
import com.gxyan.domain.StoreAddress;
import com.gxyan.domain.PcaTextArr;

/**
 * 门店Service接口
 *
 * @author cool
 * @date 2024-11-09
 */
public interface IStoreAddressService
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
     * 批量删除门店
     *
     * @param ids 需要删除的门店主键集合
     * @return 结果
     */
    public int deleteStoreAddressByIds(Long[] ids);

    /**
     * 删除门店信息
     *
     * @param id 门店主键
     * @return 结果
     */
    public int deleteStoreAddressById(Long id);
    public List<PcaTextArr> convertToPcaTextArrList(List<StoreAddress> storeAddresses);
}
