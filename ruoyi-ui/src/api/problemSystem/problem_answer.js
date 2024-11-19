import request from '@/utils/request'

// 查询提问于回答列表
export function listProblem_answer(query) {
  return request({
    url: '/problemSystem/problem_answer/list',
    method: 'get',
    params: query
  })
}

// 查询提问于回答详细
export function getProblem_answer(problemId) {
  return request({
    url: '/problemSystem/problem_answer/' + problemId,
    method: 'get'
  })
}

// 新增提问于回答
export function addProblem_answer(data) {
  return request({
    url: '/problemSystem/problem_answer',
    method: 'post',
    data: data
  })
}

// 修改提问于回答
export function updateProblem_answer(data) {
  return request({
    url: '/problemSystem/problem_answer',
    method: 'put',
    data: data
  })
}

// 删除提问于回答
export function delProblem_answer(problemId) {
  return request({
    url: '/problemSystem/problem_answer/' + problemId,
    method: 'delete'
  })
}
