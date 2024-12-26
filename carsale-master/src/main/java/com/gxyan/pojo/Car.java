package com.gxyan.pojo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Data
public class Car {
    private Long id;

    private Integer seriesId;

    private String type;

    private String color;

    private BigDecimal price;

    private BigDecimal salePrice;

    private Integer repertory;

    private Date createTime;

    private String status;

    private String imgPath;
}
