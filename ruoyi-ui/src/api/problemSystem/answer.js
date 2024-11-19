import request from '@/utils/request'

// 查询回答列表
export function listAnswer(query) {
  return request({
    url: '/problemSystem/answer/list',
    method: 'get',
    params: query
  })
}

// 查询回答详细
export function getAnswer(answerId) {
  return request({
    url: '/problemSystem/answer/' + answerId,
    method: 'get'
  })
}

// 新增回答
export function addAnswer(data) {
  return request({
    url: '/problemSystem/answer',
    method: 'post',
    data: data
  })
}

// 修改回答
export function updateAnswer(data) {
  return request({
    url: '/problemSystem/answer',
    method: 'put',
    data: data
  })
}

// 删除回答
export function delAnswer(answerId) {
  return request({
    url: '/problemSystem/answer/' + answerId,
    method: 'delete'
  })
}
