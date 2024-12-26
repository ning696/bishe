import request from "@/utils/request";
// 获取题目列表

export function getQuestionListService(params) {
  return request({
    url: "/system/question/list",
    method: "get",
    params,
  });
}

export function getQuestionDetailService(questionId) {
  return request({
    url: "/system/question/detail",
    method: "get",
    params: { questionId },
  });
}

export function addQuestionService(params = {}) {
  return request({
    url: "/system/question/add",
    method: "post",
    data: params,
  });
}

export function editQuestionService(params = {}) {
  return request({
    url: "/system/question/edit",
    method: "put",
    data: params,
  });
}

export function delQuestionService(questionId) {
  return request({
    url: "/system/question/delete",
    method: "delete",
    params: { questionId },
  });
}
