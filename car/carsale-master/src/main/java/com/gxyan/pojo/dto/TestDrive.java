package com.gxyan.pojo.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 test_drive
 *
 * @author ruoyi
 * @date 2024-11-09
 */
@Data
public class TestDrive
{
    /** 预约试驾编号 */
    private Long id;

    /** 顾客编号 */
    private Long customerId;

    /** 试驾车辆编号 */
    private Long carId;

    /** 试驾时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDriveTime;

    /** 预约状态（0：待审核，1：已审核，2：已完成，3：已取消） */
    private String status;

    /** 预约电话 */
    private String phone;

    /** 顾客姓名 */
    private String customerName;
}
