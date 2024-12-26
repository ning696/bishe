package com.gxyan.service;

import com.gxyan.common.ServerResponse;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Employee;

/**
 * @author gxyan
 * @date 2018/12/27 9:53
 */
public interface IUserService {

    ServerResponse<Employee> login(Long userId, String password);
    ServerResponse<Customer> userlogin(String userId, String password);
}
