package com.gxyan.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 门店对象 store_address
 *
 * @author cool
 * @date 2024-11-09
 */
@Data
public class StoreAddress
{
    private static final long serialVersionUID = 1L;

    /** 门店地址唯一标识 */
    private Long id;

    /** 省编码 */
    private String provinceCode;

    /** 市编码 */
    private String cityCode;

    /** 区编码 */
    private String districtCode;

    /** 具体位置（门店地址详细描述） */
    private String location;

    /** 门店联系电话 */
    private String phoneNumber;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

}
