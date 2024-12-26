package com.gxyan.controller;

import java.util.List;

import com.gxyan.domain.AjaxResult;
import com.gxyan.domain.PcaTextArr;
import com.gxyan.pojo.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gxyan.domain.StoreAddress;
import com.gxyan.service.IStoreAddressService;


/**
 * 门店Controller
 *
 * @author cool
 * @date 2024-11-09
 */
@RestController
@RequestMapping("/address")
public class StoreAddressController extends BaseController
{
    @Autowired
    private IStoreAddressService storeAddressService;

    /**
     * 查询门店列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StoreAddress storeAddress)
    {
        startPage();
        List<StoreAddress> list = storeAddressService.selectStoreAddressList(storeAddress);
        return getDataTable(list);
    }

    /**
     * 查询级联门店列表
     * @param storeAddress
     * @return
     */
    @GetMapping("/cascadeaddresslist")
    public List<PcaTextArr> cascadelist(StoreAddress storeAddress)
    {
        startPage();
        return storeAddressService.convertToPcaTextArrList(storeAddressService.selectStoreAddressList(storeAddress));
    }


    /**
     * 获取门店详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(storeAddressService.selectStoreAddressById(id));
    }

    /**
     * 新增门店
     */
    @PostMapping
    public AjaxResult add(@RequestBody StoreAddress storeAddress)
    {
        return toAjax(storeAddressService.insertStoreAddress(storeAddress));
    }

    /**
     * 修改门店
     */

    @PutMapping
    public AjaxResult edit(@RequestBody StoreAddress storeAddress)
    {
        return toAjax(storeAddressService.updateStoreAddress(storeAddress));
    }

    /**
     * 删除门店
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storeAddressService.deleteStoreAddressByIds(ids));
    }
}
