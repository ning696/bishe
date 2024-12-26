import request from '@/utils/request'

// 查询宣传列表
export function listPromotion(query) {
  return request({
    url: '/promotion/list',
    method: 'get',
    params: query
  })
}

// 查询宣传详细
export function getPromotion(id) {
  return request({
    url: '/promotion/' + id,
    method: 'get'
  })
}

// 新增宣传
export function addPromotion(data) {
  return request({
    url: '/promotion',
    method: 'post',
    data: data
  })
}

// 修改宣传
export function updatePromotion(data) {
  return request({
    url: '/promotion',
    method: 'put',
    data: data
  })
}

// 删除宣传
export function delPromotion(id) {
  return request({
    url: '/promotion/' + id,
    method: 'delete'
  })
}
