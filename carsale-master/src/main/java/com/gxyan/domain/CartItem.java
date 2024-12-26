package com.gxyan.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * user对象 cart_item
 *
 * @author cool
 * @date 2024-11-11
 */
@Data
public class CartItem
{

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    private Long userId;

    /** $column.columnComment */
    private Long carId;

    /** $column.columnComment */
    private int quantity;

    /** $column.columnComment */
    private BigDecimal price;

    /** $column.columnComment */
    private Date addedAt;

    /** $column.columnComment */
    private Date updatedAt;
}
