package cn.zc.enterprise.domain.entity;

import cn.zc.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

/**
 * 校园实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("campus")
public class Campus extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String campusName;
    private String campusCode;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String remark;
}

