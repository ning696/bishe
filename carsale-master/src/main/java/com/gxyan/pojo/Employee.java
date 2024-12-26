package com.gxyan.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Data
public class Employee {
    private Long id;

    private String role;

    private String name;

    private String password;

    private String idCard;

    private String phone;

    private String gender;

    private BigDecimal salary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entryTime;

    private String status;

    public Employee(Long id, String role, String name, String password, String idCard, String phone, String gender, BigDecimal salary, Date entryTime, String status) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.password = password;
        this.idCard = idCard;
        this.phone = phone;
        this.gender = gender;
        this.salary = salary;
        this.entryTime = entryTime;
        this.status = status;
    }

    public Employee() {
        super();
    }
}
