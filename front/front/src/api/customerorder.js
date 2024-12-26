import request from '@/utils/request'

// 查询no列表
export function listCustomerOrder(query) {
  return request({
    url: '/customerorder/list',
    method: 'get',
    params: query
  })
}

// 查询no详细
export function getCustomerOrder(customerId) {
  return request({
    url: '/customerorder/' + customerId,
    method: 'get'
  })
}

// 新增no
export function addCustomerOrder(data) {
  return request({
    url: '/customerorder',
    method: 'post',
    data: data
  })
}

// 修改no
export function updateCustomerOrder(data) {
  return request({
    url: '/customerorder',
    method: 'put',
    data: data
  })
}

// 删除no
export function delCustomerOrder(customerId) {
  return request({
    url: '/customerorder/' + customerId,
    method: 'delete'
  })
}

export function submitCommentToOrder(data) {
  return request({
    url: "/customerorder/submitComment", // 假设这是后端的提交评论接口
    method: "post",
    data
  });
}
