package com.gxyan.service.impl;

import java.util.List;

import com.gxyan.pojo.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gxyan.dao.CustomerOrderMapper;
import com.gxyan.service.ICustomerOrderService;

/**
 * noService业务层处理
 *
 * @author cool
 * @date 2024-11-14
 */
@Service
public class CustomerOrderServiceImpl implements ICustomerOrderService
{
    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 查询no
     *
     * @param customerId no主键
     * @return no
     */
    @Override
    public CustomerOrder selectCustomerOrderByCustomerId(String customerId)
    {
        return customerOrderMapper.selectCustomerOrderByCustomerId(customerId);
    }

    /**
     * 查询no列表
     *
     * @param customerOrder no
     * @return no
     */
    @Override
    public List<CustomerOrder> selectCustomerOrderList(CustomerOrder customerOrder)
    {
        return customerOrderMapper.selectCustomerOrderList(customerOrder);
    }

    /**
     * 新增no
     *
     * @param customerOrder no
     * @return 结果
     */
    @Override
    public int insertCustomerOrder(CustomerOrder customerOrder)
    {
        return customerOrderMapper.insertCustomerOrder(customerOrder);
    }

    /**
     * 修改no
     *
     * @param customerOrder no
     * @return 结果
     */
    @Override
    public int updateCustomerOrder(CustomerOrder customerOrder)
    {
        return customerOrderMapper.updateCustomerOrder(customerOrder);
    }

    /**
     * 批量删除no
     *
     * @param customerIds 需要删除的no主键
     * @return 结果
     */
    @Override
    public int deleteCustomerOrderByCustomerIds(String[] customerIds)
    {
        return customerOrderMapper.deleteCustomerOrderByCustomerIds(customerIds);
    }

    /**
     * 删除no信息
     *
     * @param customerId no主键
     * @return 结果
     */
    @Override
    public int deleteCustomerOrderByCustomerId(String customerId)
    {
        return customerOrderMapper.deleteCustomerOrderByCustomerId(customerId);
    }

    @Override
    public int submitComment(String orderDetailId, String comment) {
        return customerOrderMapper.submitComment(orderDetailId, comment);
    }
}
