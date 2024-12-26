package com.gxyan.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gxyan.common.Const;
import com.gxyan.common.ServerResponse;
import com.gxyan.dao.CarMapper;
import com.gxyan.dao.OrderDetailsMapper;
import com.gxyan.dao.OrderMapper;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Order;
import com.gxyan.pojo.OrderDetails;
import com.gxyan.pojo.dto.CartItemViewDTO;
import com.gxyan.vo.OrderDetailVo;
import com.gxyan.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.gxyan.dao.CartItemMapper;
import com.gxyan.domain.CartItem;
import com.gxyan.service.ICartItemService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * userService业务层处理
 *
 * @author ruoyi
 * @date 2024-11-11
 */
@Service
public class CartItemServiceImpl implements ICartItemService
{
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailsMapper detailsMapper;

    /**
     * 查询user
     *
     * @param id user主键
     * @return user
     */
    @Override
    public CartItem selectCartItemById(Long id)
    {
        return cartItemMapper.selectCartItemById(id);
    }

    /**
     * 查询user列表
     *
     * @param cartItem user
     * @return user
     */
    @Override
    public List<CartItem> selectCartItemList(CartItem cartItem)
    {
        return cartItemMapper.selectCartItemList(cartItem);
    }

    /**
     * 新增user
     *
     * @param cartItem user
     * @return 结果
     */
    @Override
    public int insertCartItem(CartItem cartItem)
    {
        return cartItemMapper.insertCartItem(cartItem);
    }

    /**
     * 修改user
     *
     * @param cartItem user
     * @return 结果
     */
    @Override
    public int updateCartItem(CartItem cartItem)
    {
        return cartItemMapper.updateCartItem(cartItem);
    }

    /**
     * 批量删除user
     *
     * @param ids 需要删除的user主键
     * @return 结果
     */
    @Override
    public int deleteCartItemByIds(Long[] ids)
    {
        return cartItemMapper.deleteCartItemByIds(ids);
    }

    /**
     * 删除user信息
     *
     * @param id user主键
     * @return 结果
     */
    @Override
    public int deleteCartItemById(Long id)
    {
        return cartItemMapper.deleteCartItemById(id);
    }

    @Override
    public int addCartItem(Long cartId, Long userid) {
        CartItem cartItem = new CartItem();
        cartItem.setId(createTestId());
        cartItem.setCarId(cartId);
        cartItem.setUserId(userid);
        BigDecimal l = carMapper.selectpriceByid(cartId);
        cartItem.setPrice(l);
        cartItem.setQuantity(1);

        return cartItemMapper.insertCartItem(cartItem);
    }

    @Override
    public List<CartItem> selectCartItemListByCustomerId(Long customerId) {
        return cartItemMapper.selectCartItemListByCustomerId(customerId);
    }

    private Long createTestId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "006000";
        return Long.valueOf(format) + (num++);
    }

    @Override
    public List<CartItemViewDTO> selectCartItemViewListByCustomerId(Long id) {
        List<CartItemViewDTO> cartItemViewDTOS = cartItemMapper.selectCartItemViewListByCustomerId(id);
        return cartItemViewDTOS;
    }

    @Override
    public int addcartquantity(Long cartId) {
        int addcartquantity = cartItemMapper.addcartquantity(cartId);
        return addcartquantity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse checkUserout(Long userId) {

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        List<CartItem> list = cartItemMapper.selectCartItemList(cartItem);
        //判断余额是否足够
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItem item : list) {
            totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        Customer user = cartItemMapper.selectCustomerByUserId(userId);
        if (user.getBalance().compareTo(totalPrice) < 0){
            return ServerResponse.createByErrorMessage("余额不足，请充值");
        }
        OrderVo orderVo = new OrderVo();
        orderVo.setCustomerId(userId);
        orderVo.setEmployeeId(2411003);
        orderVo.setStatus(Const.Number.ONE);
        orderVo.setTotalPrice(totalPrice);
        List<OrderDetailVo> detailVos = new ArrayList<>();
        for (CartItem item : list) {
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            orderDetailVo.setCarId(item.getCarId());
            orderDetailVo.setCarNumber(item.getQuantity());
            detailVos.add(orderDetailVo);
        }
        orderVo.setDetailVos(detailVos);
        //扣除库存
//        for (CartItem item : list) {
//            int result=0;
//            int num = carMapper.selectRepertoryByPrimaryKey(item.getCarId()) - item.getQuantity();
//            if (num >= 0){
//                result = carMapper.updateRepertoryByPrimaryKey(item.getCarId(), num);
//                if (result == 0) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return ServerResponse.createByErrorMessage("添加订单失败");
//                }
//            } else {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                CartItemViewDTO cartItemViewDTO = cartItemMapper.selectCartItemNameById(item.getCarId());
//                return ServerResponse.createByErrorMessage(cartItemViewDTO.getBrandName()+cartItemViewDTO.getSeriesName()+cartItemViewDTO.getType()+"库存不足，添加订单失败");
//            }
//        }
        Order order = new Order();
        Long orderId =  createOrderId();
        order.setId(orderId);
        order.setCustomerId(orderVo.getCustomerId());
        order.setEmployeeId(orderVo.getEmployeeId());
        order.setStatus(orderVo.getStatus());
        order.setTotalPrice(orderVo.getTotalPrice());
        int result = orderMapper.insert(order);
        if (result == 0) {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("添加订单失败");
        }
        if (orderVo.getStatus().equals(Const.Number.ONE)) {
            result = orderMapper.updatePayTimeByPrimaryKey(orderId);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("添加订单失败");
            }
        }
        for (OrderDetailVo detailVo: orderVo.getDetailVos()) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setId(createOrderDetailsId());
            orderDetails.setCarId(detailVo.getCarId());
            int num = detailVo.getCarNumber();
            orderDetails.setCarNumber(num);
            orderDetails.setOrderId(orderId);
            result = detailsMapper.insert(orderDetails);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("添加订单失败");
            }
            int repertory = carMapper.selectRepertoryByPrimaryKey(detailVo.getCarId());
            num = repertory - num;
            if (num >= 0){
                result = carMapper.updateRepertoryByPrimaryKey(detailVo.getCarId(), num);
                if (result == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByErrorMessage("添加订单失败");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("库存不足，添加订单失败");
            }
        }
        // 扣除余额
        result = cartItemMapper.updateBalanceByUserId(userId, totalPrice);
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("余额不足，请充值");
        }
        // 清空购物车
        result = cartItemMapper.deleteCartItemByUserId(userId);
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("添加订单失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public int delcartquantity(Long cartId) {
        int currentQuantity = cartItemMapper.getCartItemQuantityById(cartId);
        if (currentQuantity > 1) {
            return cartItemMapper.delcartquantity(cartId);
        }else if (currentQuantity == 1){
            return cartItemMapper.deleteCartItemById(cartId);
        }else {
            return 0;
        }

    }

    private int num = 1;

    @Scheduled(cron="0 0 0 * * ?")
    private void clearNum() {
        num = 1;
    }
    /**
     * 订单编号
     * 格式为：yyMMdd 加6位递增的数字，数字每天重置为1
     * @return
     */
    private Long createOrderId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "0658000";
        return Long.valueOf(format) + (num++);
    }
    private String createOrderDetailsId() {
        int first = new Random(10).nextInt(8) + 1;
        System.out.println(first);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return first + String.format("%015d", hashCodeV);
    }

}
