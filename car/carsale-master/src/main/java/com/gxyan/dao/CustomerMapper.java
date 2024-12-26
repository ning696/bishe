package com.gxyan.dao;

import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Employee;
import com.gxyan.vo.CustomerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    List<Customer> selectSelective(CustomerQuery customerQuery);

    Customer selectByIdCard(String idCard);

    Customer selectLogin(@Param("phone") String phone, @Param("password") String password);

    int checkUserPhone(String phone);
}
