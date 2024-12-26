package com.gxyan.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gxyan.common.Const;
import com.gxyan.common.ServerResponse;
import com.gxyan.domain.AjaxResult;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.TableDataInfo;
import com.gxyan.pojo.dto.CartItemViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.gxyan.domain.CartItem;
import com.gxyan.service.ICartItemService;

/**
 * userController
 *
 * @author ruoyi
 * @date 2024-11-11
 */
@RestController
@RequestMapping("/cartItem")
public class CartItemController extends BaseController
{
    @Autowired
    private ICartItemService cartItemService;

    /**
     * 查询user列表
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ServerResponse list(HttpSession session)
    {
        Customer user = (Customer)session.getAttribute(Const.CURRENT_USER);
        List<CartItemViewDTO> list = cartItemService.selectCartItemViewListByCustomerId(user.getId());
        return ServerResponse.createBySuccess(list);
    }
    //结算接口
    @RequestMapping(value = "checkUserout", method = RequestMethod.POST)
    public ServerResponse checkUserout(Long userId)
    {
        return cartItemService.checkUserout(userId);
    }
//    /**
//     * 导出user列表
//     */
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, CartItem cartItem)
//    {
//        List<CartItem> list = cartItemService.selectCartItemList(cartItem);
//        ExcelUtil<CartItem> util = new ExcelUtil<CartItem>(CartItem.class);
//        util.exportExcel(response, list, "user数据");
//    }

    /**
     * 获取user详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cartItemService.selectCartItemById(id));
    }

    /**
     * 新增user
     */
    @PostMapping("/add")
    public ServerResponse add(@RequestBody Map<String, Object> request) {
        Long cartId = (Long)request.get("cartId");
        Long userid = (Long)request.get("userid");
        int i = cartItemService.addCartItem(cartId, userid);
        return ServerResponse.createBySuccess();
    }
    @PostMapping("/addcartquantity")
    public ServerResponse addcartquantity(@RequestParam Long cartId) {
        cartItemService.addcartquantity(cartId);
        return ServerResponse.createBySuccess();
    }
    @PostMapping("/delItemquantity")
    public ServerResponse delItemquantity(@RequestParam Long cartId) {
        int delcartquantity = cartItemService.delcartquantity(cartId);
        return delcartquantity != 0 ? ServerResponse.createBySuccess() : ServerResponse.createByError();
    }
    /**
     * 修改user
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CartItem cartItem)
    {
        return toAjax(cartItemService.updateCartItem(cartItem));
    }

    /**
     * 删除user
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cartItemService.deleteCartItemByIds(ids));
    }

}
