import request from '@/utils/request'

export function loginByUsername(employeeId, password, roles) {
  return request({
    url: '/user/login',
    method: 'post',
    params: { employeeId, password, roles }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'get'
  })
}

export function getUserInfo(token) {
  return request({
    url: '/user/info',
    method: 'post',
    params: { token }
  })
}
