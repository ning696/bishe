package com.gxyan.dao;

import com.gxyan.pojo.CustomerOrder;

import java.util.List;

/**
 * noMapper接口
 *
 * @author cool
 * @date 2024-11-14
 */
public interface CustomerOrderMapper
{
    /**
     * 查询no
     *
     * @param customerId no主键
     * @return no
     */
    public CustomerOrder selectCustomerOrderByCustomerId(String customerId);

    /**
     * 查询no列表
     *
     * @param customerOrder no
     * @return no集合
     */
    public List<CustomerOrder> selectCustomerOrderList(CustomerOrder customerOrder);

    /**
     * 新增no
     *
     * @param customerOrder no
     * @return 结果
     */
    public int insertCustomerOrder(CustomerOrder customerOrder);

    /**
     * 修改no
     *
     * @param customerOrder no
     * @return 结果
     */
    public int updateCustomerOrder(CustomerOrder customerOrder);

    /**
     * 删除no
     *
     * @param customerId no主键
     * @return 结果
     */
    public int deleteCustomerOrderByCustomerId(String customerId);

    /**
     * 批量删除no
     *
     * @param customerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerOrderByCustomerIds(String[] customerIds);

    int submitComment(String orderDetailId, String comment);
}
