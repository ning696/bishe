import service from "@/utils/request";

export function getUserInfoService() {
  return service({
    url: "/system/sysUser/info",
    method: "get",
  });
}
