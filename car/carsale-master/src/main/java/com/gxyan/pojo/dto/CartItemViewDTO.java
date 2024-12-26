package com.gxyan.pojo.dto;

import lombok.Data;

@Data
public class CartItemViewDTO {
    private Long id;
    private String brandName;     // 品牌名称
    private String seriesName;    // 车系名称
    private String type;          // 车辆型号
    private String color;         // 车辆颜色
    private Double salePrice;     // 车辆售价
    private Integer quantity;     // 商品数量
}
