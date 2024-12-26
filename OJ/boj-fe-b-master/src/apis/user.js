import request from "@/utils/request";

export function getUserListService(params) {
  return request({
    url: "/system/user/list",
    method: "get",
    params,
  });
}

export function updateStatusService(params = {}) {
  return request({
    url: "/system/user/updateStatus",
    method: "put",
    data: params,
  });
}