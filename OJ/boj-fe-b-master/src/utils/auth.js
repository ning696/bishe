import Cookies from "js-cookie";
// 区分开B端和C端的token
const TokenKey = "Admin-Oj-b-Token";

export function getToken() {
  return Cookies.get(TokenKey);
}

export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  return Cookies.remove(TokenKey);
}
