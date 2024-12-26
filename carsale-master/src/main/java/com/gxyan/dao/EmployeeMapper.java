package com.gxyan.dao;

import com.gxyan.pojo.Employee;
import com.gxyan.vo.EmployeeQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    Long checkUserId(Long id);

    Employee selectLogin(@Param("id") Long id, @Param("password") String password);

    List<Employee> selectSelective(EmployeeQuery employeeQuery);

    String selectPasswordByPrimaryKey(Long id);

    int updatePasswordByPrimaryKeyAndOldPass(@Param("id") Long id, @Param("oldPass") String oldPass, @Param("newPass") String newPass);

    int updateCustomerPasswordByPrimaryKeyAndOldPass(@Param("id") Long id, @Param("oldPass") String oldPass, @Param("newPass") String newPass);

    String selectCustomerPasswordByPrimaryKey(Long id);
}
