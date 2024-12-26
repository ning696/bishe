import request from "@/utils/request";

export function getExamListService(params) {
  return request({
    url: "/system/exam/list",
    method: "get",
    params,
  });
}

export function getExamDetailService(examId) {
  return request({
    url: "/system/exam/detail",
    method: "get",
    params: { examId },
  });
}

export function getExamQuestionListService(examId) {
  return request({
    url: "/system/exam/question/list",
    method: "get",
    params: { examId },
  });
}

export function delExamService(examId) {
  return request({
    url: "/system/exam/delete",
    method: "delete",
    params: { examId },
  });
}

export function saveBaseInfoService(params = {}) {
  return request({
    url: "/system/exam/baseInfo/save",
    method: "post",
    data: params,
  });
}

export function updateStatusService(params = {}) {
  return request({
    url: "/system/exam/updateStatus",
    method: "put",
    data: params,
  });
}

export function publishService(params = {}) {
  return request({
    url: "/system/exam/publish",
    method: "put",
    data: params,
  });
}

export function cancelPublishService(params = {}) {
  return request({
    url: "/system/exam/cancelPublish",
    method: "put",
    data: params,
  });
}



export function addExamQuestionService(params = {}) {
  return request({
    url: "/system/exam/question/add",
    method: "post",
    data: params,
  });
}

export function delExamQuestionService(examId, questionId) {
  return request({
    url: "/system/exam/question/delete",
    method: "delete",
    params: { examId, questionId },
  });
}