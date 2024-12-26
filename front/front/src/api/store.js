import request from '../utils/request'

export function addBrand(brand) {
  return request({
    url: '/store/addBrand',
    method: 'get',
    params: { brand }
  })
}

export function delBrand(brandId) {
  return request({
    url: '/store/delBrand',
    method: 'get',
    params: { brandId }
  })
}

export function addSeries(brandId, seriesName) {
  return request({
    url: '/store/addSeries',
    method: 'get',
    params: { brandId, seriesName }
  })
}

export function delSeries(seriesId) {
  return request({
    url: '/store/delSeries',
    method: 'get',
    params: { seriesId }
  })
}

export function addStore(formData) {
  // console.log(car)
  return request({
    url: '/store/addStore',
    method: 'post',
    data: formData
  })
}

export function fetchList(query) {
  // console.log(query)
  return request({
    url: '/store/getList',
    method: 'get',
    params: query
  })
}

export const insertPromotion = (form) => {
  return request({
    url: '/store/insertPromotion',
    method: 'post',
    data: form
  })
}
// 预约试驾 API
export const bookTestDriveApi = (form) => {
  return request({
    url: '/testdrive/bookTestDrive',
    method: 'post',
    data: form
  })
}

export function updatepromotion(data) {
  console.log(data)
  return request({
    url: '/promotion/update',
    method: 'post',
    data: data
  })
}

