package com.gxyan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gxyan.domain.PcaTextArr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gxyan.dao.StoreAddressMapper;
import com.gxyan.domain.StoreAddress;
import com.gxyan.domain.Localtion;
import com.gxyan.service.IStoreAddressService;

/**
 * 门店Service业务层处理
 *
 * @author cool
 * @date 2024-11-09
 */
@Service
public class StoreAddressServiceImpl implements IStoreAddressService
{
    @Autowired
    private StoreAddressMapper storeAddressMapper;

    /**
     * 查询门店
     *
     * @param id 门店主键
     * @return 门店
     */
    @Override
    public StoreAddress selectStoreAddressById(Long id)
    {
        return storeAddressMapper.selectStoreAddressById(id);
    }

    /**
     * 查询门店列表
     *
     * @param storeAddress 门店
     * @return 门店
     */
    @Override
    public List<StoreAddress> selectStoreAddressList(StoreAddress storeAddress)
    {
        return storeAddressMapper.selectStoreAddressList(storeAddress);
    }

    /**
     * 新增门店
     *
     * @param storeAddress 门店
     * @return 结果
     */
    @Override
    public int insertStoreAddress(StoreAddress storeAddress)
    {
        return storeAddressMapper.insertStoreAddress(storeAddress);
    }

    /**
     * 修改门店
     *
     * @param storeAddress 门店
     * @return 结果
     */
    @Override
    public int updateStoreAddress(StoreAddress storeAddress)
    {
        return storeAddressMapper.updateStoreAddress(storeAddress);
    }

    /**
     * 批量删除门店
     *
     * @param ids 需要删除的门店主键
     * @return 结果
     */
    @Override
    public int deleteStoreAddressByIds(Long[] ids)
    {
        return storeAddressMapper.deleteStoreAddressByIds(ids);
    }

    /**
     * 删除门店信息
     *
     * @param id 门店主键
     * @return 结果
     */
    @Override
    public int deleteStoreAddressById(Long id)
    {
        return storeAddressMapper.deleteStoreAddressById(id);
    }
    public List<PcaTextArr> convertToPcaTextArrList(List<StoreAddress> storeAddresses) {
        // 用来存放转换后的结果
        List<PcaTextArr> pcaTextArrList = new ArrayList<>();

        // 省 -> 市 -> 区 -> 门店位置
        Map<String, Map<String, Map<String, List<StoreAddress>>>> groupedAddresses = new HashMap<>();

        // 按省、市、区分组
        for (StoreAddress store : storeAddresses) {
            String provinceCode = store.getProvinceCode();
            String cityCode = store.getCityCode();
            String districtCode = store.getDistrictCode();

            // 将门店按省、市、区进行分组
            groupedAddresses
                    .computeIfAbsent(provinceCode, k -> new HashMap<>())
                    .computeIfAbsent(cityCode, k -> new HashMap<>())
                    .computeIfAbsent(districtCode, k -> new ArrayList<>())
                    .add(store);
        }

        // 遍历分组后的数据并转换为 pcaTextArrList
        for (Map.Entry<String, Map<String, Map<String, List<StoreAddress>>>> provinceEntry : groupedAddresses.entrySet()) {
            String provinceCode = provinceEntry.getKey();
            Map<String, Map<String, List<StoreAddress>>> cityMap = provinceEntry.getValue();

            PcaTextArr provinceArr = new PcaTextArr();
            provinceArr.setLabel(provinceCode); // 设置省名称
            provinceArr.setValue(provinceCode); // 设置省编码

            // 遍历城市
            for (Map.Entry<String, Map<String, List<StoreAddress>>> cityEntry : cityMap.entrySet()) {
                String cityCode = cityEntry.getKey();
                Map<String, List<StoreAddress>> districtMap = cityEntry.getValue();

                PcaTextArr cityArr = new PcaTextArr();
                cityArr.setLabel(cityCode);  // 设置市名称
                cityArr.setValue(cityCode);  // 设置市编码

                // 遍历区
                for (Map.Entry<String, List<StoreAddress>> districtEntry : districtMap.entrySet()) {
                    String districtCode = districtEntry.getKey();

                    PcaTextArr districtArr = new PcaTextArr();
                    districtArr.setLabel(districtCode);  // 设置区名称
                    districtArr.setValue(districtCode);  // 设置区编码

                    // 遍历门店位置，添加到区的 children 中
                    for (StoreAddress store : districtEntry.getValue()) {
                        Localtion location = new Localtion();
                        location.setLabel(store.getLocation()); // 设置门店位置
                        location.setValue(store.getLocation()); // 设置门店位置


                        districtArr.getChildren().add(location); // 将位置添加到区的 children 中
                    }

                    cityArr.getChildren().add(districtArr);  // 将区添加到市的 children 中
                }

                provinceArr.getChildren().add(cityArr);  // 将城市添加到省的 children 中
            }

            pcaTextArrList.add(provinceArr);  // 将省添加到最终的结果列表中
        }

        return pcaTextArrList;
    }
}
