package com.gxyan.service;

import java.util.List;

import com.gxyan.common.ServerResponse;
import com.gxyan.domain.CartItem;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.dto.CartItemViewDTO;

/**
 * userService接口
 *
 * @author ruoyi
 * @date 2024-11-11
 */
public interface ICartItemService
{
    /**
     * 查询user
     *
     * @param id user主键
     * @return user
     */
    public CartItem selectCartItemById(Long id);

    /**
     * 查询user列表
     *
     * @param cartItem user
     * @return user集合
     */
    public List<CartItem> selectCartItemList(CartItem cartItem);

    /**
     * 新增user
     *
     * @param cartItem user
     * @return 结果
     */
    public int insertCartItem(CartItem cartItem);

    /**
     * 修改user
     *
     * @param cartItem user
     * @return 结果
     */
    public int updateCartItem(CartItem cartItem);

    /**
     * 批量删除user
     *
     * @param ids 需要删除的user主键集合
     * @return 结果
     */
    public int deleteCartItemByIds(Long[] ids);

    /**
     * 删除user信息
     *
     * @param id user主键
     * @return 结果
     */
    public int deleteCartItemById(Long id);

    int addCartItem(Long cartId, Long userid);

    List<CartItem> selectCartItemListByCustomerId(Long customerId);

    List<CartItemViewDTO> selectCartItemViewListByCustomerId(Long id);

    int addcartquantity(Long cartId);

    int delcartquantity(Long cartId);

    ServerResponse checkUserout(Long user);
}
