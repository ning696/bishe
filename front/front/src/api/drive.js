import request from '@/utils/request'

// 查询drive列表
export function listDrive(query) {
  return request({
    url: '/testdrive/list',
    method: 'get',
    params: query
  })
}

// 查询drive详细
export function getDrive(id) {
  return request({
    url: '/system/drive/' + id,
    method: 'get'
  })
}

// 新增drive
export function addDrive(data) {
  return request({
    url: '/system/drive',
    method: 'post',
    data: data
  })
}

// 修改drive
export function updateDrive(data) {
  return request({
    url: '/testdrive',
    method: 'put',
    data: data
  })
}
// 用户查看预约列表
export function getuserdriverList(token) {
  return request({
    url: '/testdrive/userdriver',
    method: 'post',
    params: { token }
  })
}
// 删除drive
export function delDrive(id) {
  return request({
    url: '/drive/' + id,
    method: 'delete'
  })
}
export function refuse(ids) {
  return request({
    url: '/testdrive/refuse',
    method: 'post',
    data: { ids }
  })
}
