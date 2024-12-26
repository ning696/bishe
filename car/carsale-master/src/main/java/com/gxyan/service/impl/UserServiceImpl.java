package com.gxyan.service.impl;

import com.gxyan.common.ServerResponse;
import com.gxyan.dao.CustomerMapper;
import com.gxyan.dao.EmployeeMapper;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Employee;
import com.gxyan.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gxyan
 * @date 2018/12/27 9:56
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CustomerMapper customerMapper;



    @Override
    public ServerResponse<Employee> login(Long userId, String password) {
        Long resultCount = employeeMapper.checkUserId(userId);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        Employee employee = employeeMapper.selectLogin(userId, password);
        if (employee == null) {
            // 查询结果为空
            return ServerResponse.createByErrorMessage("密码错误");
        }
        employee.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", employee);
    }
    @Override
    public ServerResponse<Customer> userlogin(String phone, String password) {
        int resultCount = customerMapper.checkUserPhone(phone);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        Customer customer = customerMapper.selectLogin(phone, password);
        if (customer == null) {
            // 查询结果为空
            return ServerResponse.createByErrorMessage("密码错误");
        }
        customer.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", customer);
    }
}
