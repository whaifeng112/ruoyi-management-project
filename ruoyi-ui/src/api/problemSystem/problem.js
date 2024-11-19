import request from '@/utils/request'

// 查询问题列表
export function listProblem(query) {
  return request({
    url: '/problemSystem/problem/list',
    method: 'get',
    params: query
  })
}

// 查询问题详细
export function getProblem(problemId) {
  return request({
    url: '/problemSystem/problem/' + problemId,
    method: 'get'
  })
}

// 新增问题
export function addProblem(data) {
  return request({
    url: '/problemSystem/problem',
    method: 'post',
    data: data
  })
}

// 修改问题
export function updateProblem(data) {
  return request({
    url: '/problemSystem/problem',
    method: 'put',
    data: data
  })
}

// 删除问题
export function delProblem(problemId) {
  return request({
    url: '/problemSystem/problem/' + problemId,
    method: 'delete'
  })
}
