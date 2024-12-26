package com.gxyan.pojo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class Customer {
    private Long id;

    private String name;

    private String phone;

    private String idCard;

    private Date createTime;

    private String password;

    private String role;

    private BigDecimal balance;

    public Customer(Long id, String name, String phone, String idCard, Date createTime, String password, String role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
        this.createTime = createTime;
        this.password = password;
        this.role = role;
    }

    public Customer() {
    }
}
