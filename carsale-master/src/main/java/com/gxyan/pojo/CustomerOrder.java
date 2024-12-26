package com.gxyan.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class CustomerOrder {

    private Long orderId;            // 订单编号
    private String carType;          // 车辆型号
    private String carSeries;        // 车系名称
    private Integer quantity;        // 车辆数量
    private BigDecimal unitPrice;    // 每辆车的单价
    private LocalDateTime orderDate; // 订单创建时间
    private String orderStatus;      // 订单状态
    private String customerId;     // 客户姓名
    private String orderDetailId;

}
