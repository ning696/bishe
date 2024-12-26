import service from "@/utils/request";

export function loginServeice(userAccount, password) {
  return service({
    url: "/system/sysUser/login",
    headers: {
      isToken: false,
    },
    method: "post",
    data: { userAccount, password },
  });
}


export function logout() {
  return service({
    url: "/system/sysUser/logout",
    method: "delete"
  });
}