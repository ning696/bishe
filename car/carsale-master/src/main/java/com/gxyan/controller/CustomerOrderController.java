package com.gxyan.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.gxyan.common.ServerResponse;
import com.gxyan.domain.AjaxResult;
import com.gxyan.pojo.CustomerOrder;
import com.gxyan.pojo.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gxyan.service.ICustomerOrderService;


/**
 * noController
 *
 * @author cool
 * @date 2024-11-14
 */
@RestController
@RequestMapping("/customerorder")
public class CustomerOrderController extends BaseController
{
    @Autowired
    private ICustomerOrderService customerOrderService;

    /**
     * 查询no列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CustomerOrder customerOrder)
    {
        startPage();
        List<CustomerOrder> list = customerOrderService.selectCustomerOrderList(customerOrder);
        return getDataTable(list);
    }

//    /**
//     * 导出no列表
//     */
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, CustomerOrder customerOrder)
//    {
//        List<CustomerOrder> list = customerOrderService.selectCustomerOrderList(customerOrder);
//        ExcelUtil<CustomerOrder> util = new ExcelUtil<CustomerOrder>(CustomerOrder.class);
//        util.exportExcel(response, list, "no数据");
//    }

    /**
     * 获取no详细信息
     */
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") String customerId)
    {
        return success(customerOrderService.selectCustomerOrderByCustomerId(customerId));
    }

    /**
     * 新增no
     */
    @PostMapping
    public AjaxResult add(@RequestBody CustomerOrder customerOrder)
    {
        return toAjax(customerOrderService.insertCustomerOrder(customerOrder));
    }
    @PostMapping("/submitComment")
    public ServerResponse add(@RequestBody Map<String, Object> request) {
        String orderDetailId = (String)request.get("orderDetailId");
        String comment = (String) request.get("comment");
        int i = customerOrderService.submitComment(orderDetailId, comment);
        if (i == 0) {
            return ServerResponse.createByErrorMessage("评论失败");
        }
        return ServerResponse.createBySuccess();
    }
    /**
     * 修改no
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerOrder customerOrder)
    {
        return toAjax(customerOrderService.updateCustomerOrder(customerOrder));
    }

    /**
     * 删除no
     */
	@DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable String[] customerIds)
    {
        return toAjax(customerOrderService.deleteCustomerOrderByCustomerIds(customerIds));
    }
}
