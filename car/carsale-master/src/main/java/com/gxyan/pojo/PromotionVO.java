package com.gxyan.pojo;

import lombok.Data;

import java.sql.Timestamp;
@Data

public class PromotionVO {
    private int promotionId;
    private String promotionText;
    private String brandName;
    private String seriesName;
    private String carType;
    private String carColor;
    private int carInventory;
    private String carStatus;
    private String carImageUrl;
}
