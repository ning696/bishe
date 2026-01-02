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
 * 企业实体类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
@TableName("enterprise")
public class Enterprise extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String enterpriseName;
    private String legalPerson;
    private String phone;
    private String email;
    private String logo;
    private String address;
    private String industry;
    private String scale;
    private String description;
    private String website;
    private Integer certificationStatus;
    private String certificationFile;
    private Integer status;
    private String remark;
}

