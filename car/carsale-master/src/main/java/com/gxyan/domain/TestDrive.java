package com.gxyan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
    @Data
    public class TestDrive {
        private static final long serialVersionUID = 1L;
        /** 试驾时间 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date testDriveTime;

        /** 预约状态（0：待审核，1：已审核，2：已完成，3：已取消） */
        private String status;

        /** 预约电话 */
        private String phone;

        /** 顾客姓名 */
        private String customerName;

        /** 车辆型号 */
        private String carModel;

        /** 车辆颜色 */
        private String carColor;

        /** 车辆售价 */
        private BigDecimal price; // 适当选择 BigDecimal 以表示货币
    }
