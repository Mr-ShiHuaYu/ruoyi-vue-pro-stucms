import request from '@/utils/request'

// 查询成绩列表
export function listScore(query) {
  return request({
    url: '/stucms/score/page',
    method: 'get',
    params: query
  })
}

// 查询成绩详细
export function getScore(scoreId) {
  return request({
    url: '/stucms/score/' + scoreId,
    method: 'get'
  })
}

// 新增成绩
export function addScore(data) {
  return request({
    url: '/stucms/score',
    method: 'post',
    data: data
  })
}

// 修改成绩
export function updateScore(data) {
  return request({
    url: '/stucms/score',
    method: 'put',
    data: data
  })
}

// 删除成绩
export function delScore(scoreId) {
  return request({
    url: '/stucms/score/' + scoreId,
    method: 'delete'
  })
}
