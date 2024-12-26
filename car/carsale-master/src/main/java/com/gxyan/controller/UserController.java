package com.gxyan.controller;

import com.gxyan.common.Const;
import com.gxyan.common.ServerResponse;
import com.gxyan.dao.CustomerMapper;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Employee;
import com.gxyan.service.IEmployeeService;
import com.gxyan.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author gxyan
 * @date 2018/12/26 20:39
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private CustomerMapper customerMapper;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse login( String employeeId, String password, String roles, HttpSession session) {
        ServerResponse response =new ServerResponse();
        if(roles.equals("用户")){
            response = userService.userlogin(employeeId, password);
        }else {
            response = userService.login(Long.valueOf(employeeId), password);
        }
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());

            Map<String, String> map = new HashMap <>(1);
            map.put("token", session.getId());
            response = ServerResponse.createBySuccess(map);
        }
        log.info("userId:{}, password:{}, data:{}", employeeId, password, response.getData());
        return response;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "info", method = RequestMethod.POST)
    public ServerResponse info(HttpSession session) {
        Object user = session.getAttribute(Const.CURRENT_USER);
        if (user instanceof Employee) {
            return ServerResponse.createBySuccess((Employee) user);
        } else if (user instanceof Customer) {
            return ServerResponse.createBySuccess((Customer) user);
        }
        return ServerResponse.createByErrorMessage("无法获取用户信息");
    }


    @RequestMapping(value = "updateMessage", method = RequestMethod.POST)
    public ServerResponse updateMessage(Employee employee) {
        if (Objects.equals(employee.getRole(), "3")){
            Customer customer = new Customer();
            customer.setName(employee.getName());
            customer.setPhone(employee.getPhone());
            customer.setId(employee.getId());
            int i = customerMapper.updateByPrimaryKeySelective(customer);
            return i > 0? ServerResponse.createBySuccess("修改成功") : ServerResponse.createByErrorMessage("修改失败");
        }
        return employeeService.updateEmployee(employee);
    }

    @RequestMapping(value = "validPassword", method = RequestMethod.POST)
    public ServerResponse validPassword(HttpSession session, String validPass) {
//        Employee employee = (Employee) session.getAttribute(Const.CURRENT_USER);
//        return employeeService.validPassword(employee.getId(), validPass);
        Object user = session.getAttribute(Const.CURRENT_USER);
        if (user instanceof Employee) {
            Employee employee = (Employee) user;
            return employeeService.validPassword(employee.getId(), validPass);
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            return employeeService.validCustomerPassword(customer.getId(), validPass);
        }
        return ServerResponse.createByErrorMessage("无法获取用户信息");
    }

    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    public ServerResponse updatePassword(HttpSession session, String oldPass, String newPass) {
//        Employee employee = (Employee) session.getAttribute(Const.CURRENT_USER);
//        return employeeService.updatePassword(employee.getId(), oldPass, newPass);
        Object user = session.getAttribute(Const.CURRENT_USER);
        if (user instanceof Employee) {
            Employee employee = (Employee) user;
            return employeeService.updatePassword(employee.getId(), oldPass, newPass);
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            return employeeService.updateCustomerPassword(customer.getId(), oldPass, newPass);
        }
        return ServerResponse.createByErrorMessage("无法获取用户信息");
    }
}
