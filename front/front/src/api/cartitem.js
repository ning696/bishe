import request from '@/utils/request'

// 查询user列表
export function listItem(token) {
  return request({
    url: '/cartItem/list',
    method: 'post',
    params: { token }
  })
}
export function addcartquantity(cartId) {
  return request({
    url: '/cartItem/addcartquantity',
    method: 'post',
    params: { cartId }
  })
}
export function delItemquantity(cartId) {
  return request({
    url: '/cartItem/delItemquantity',
    method: 'post',
    params: { cartId }
  })
}
// 查询user详细
export function getItem(id) {
  return request({
    url: '/system/item/' + id,
    method: 'get'
  })
}

// 新增user
export function addItem(data) {
  return request({
    url: '/system/item',
    method: 'post',
    data: data
  })
}
// 加入购物车 API
export const addToCartApi = (cartId, userid) => {
  return request({
    url: '/cartItem/add',
    method: 'post',
    data: { cartId, userid }
  })
}
// 修改user
export function updateItem(data) {
  return request({
    url: '/system/item',
    method: 'put',
    data: data
  })
}

// 删除user
export function delItem(id) {
  return request({
    url: '/system/item/' + id,
    method: 'delete'
  })
}
export function checkUserout(userId) {
  return request({
    url: '/cartItem/checkUserout',
    method: 'post',
    params: { userId }
  })
}
