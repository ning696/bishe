package com.gxyan.pojo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 宣传对象 promotion
 *
 * @author ruoyi
 * @date 2024-11-14
 */
public class Promotion
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 车辆编号 */
    private Long carId;

    /** 宣传词 */
    private String promotionText;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setPromotionText(String promotionText)
    {
        this.promotionText = promotionText;
    }

    public String getPromotionText()
    {
        return promotionText;
    }
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carId", getCarId())
            .append("promotionText", getPromotionText())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
