import request from '@/utils/request'

// 查询门店列表
export function listAddress(query) {
  return request({
    url: '/address/list',
    method: 'get',
    params: query
  })
}
// 查询级联门店列表
export function cascadeaddresslist(query) {
  return request({
    url: '/address/cascadeaddresslist',
    method: 'get',
    params: query
  })
}
// 查询门店详细
export function getAddress(id) {
  return request({
    url: '/address/' + id,
    method: 'get'
  })
}

// 新增门店
export function addAddress(data) {
  return request({
    url: '/address',
    method: 'post',
    data: data
  })
}

// 修改门店
export function updateAddress(data) {
  return request({
    url: '/system/address',
    method: 'put',
    data: data
  })
}

// 删除门店
export function delAddress(id) {
  return request({
    url: '/address/' + id,
    method: 'delete'
  })
}
