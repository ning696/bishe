package com.gxyan.service;

import com.gxyan.common.ServerResponse;
import com.gxyan.pojo.Car;
import com.gxyan.vo.StoreQuery;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

/**
 * @author gxyan
 * @date 2019/1/3 10:07
 */
public interface IStoreService {

    ServerResponse addBrand(String brandName);

    ServerResponse delBrand(Integer brandId);

    ServerResponse addSeries(Integer brandId, String seriesName);

    ServerResponse delSeries(Integer seriesId);

    ServerResponse addStore(String car, List<MultipartFile> files);

    ServerResponse getList(StoreQuery storeQuery);

    ServerResponse updateStore(Car car);

    ServerResponse insertPromotion(String promotionText, Long carId);
}
