package com.gxyan.controller;

import com.gxyan.common.ServerResponse;
import com.gxyan.pojo.Car;
import com.gxyan.service.IStoreService;
import com.gxyan.vo.StoreQuery;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author gxyan
 * @date 2019/1/3 10:04
 */
@Slf4j
@RestController
@RequestMapping("store")
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @RequestMapping(value = "addBrand", method = RequestMethod.GET)
    public ServerResponse addBrand(String brand) {
        return storeService.addBrand(brand);
    }

    @RequestMapping(value = "delBrand", method = RequestMethod.GET)
    public ServerResponse delBrand(Integer brandId) {
        return storeService.delBrand(brandId);
    }

    @RequestMapping(value = "addSeries", method = RequestMethod.GET)
    public ServerResponse addSeries(Integer brandId, String seriesName) {
        return storeService.addSeries(brandId, seriesName);
    }

    @RequestMapping(value = "delSeries", method = RequestMethod.GET)
    public ServerResponse delSeries(Integer seriesId) {
        return storeService.delSeries(seriesId);
    }

    @RequestMapping(value = "addStore", method = RequestMethod.POST)
    public ServerResponse addStore(@RequestParam("car") String carJson, // 普通表单数据
                                   @RequestParam("files") List<MultipartFile> files) throws IOException {
        return storeService.addStore(carJson, files);
    }

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    public ServerResponse getList(StoreQuery storeQuery) {
        return storeService.getList(storeQuery);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ServerResponse update(Car car) {
        return storeService.updateStore(car);
    }
    @RequestMapping(value = "insertPromotion", method = RequestMethod.POST)
    public ServerResponse insertPromotion(@RequestBody Map<String, Object> map) {
        String promotionText = (String)map.get("promotionText");
        Long carId = (Long)map.get("id");
        return storeService.insertPromotion(promotionText, carId);
    }
}
