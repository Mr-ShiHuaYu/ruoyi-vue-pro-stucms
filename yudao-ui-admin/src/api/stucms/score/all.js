import request from '@/utils/request'

// 查询成绩列表
export function listAllScore(query) {
  return request({
    url: '/stucms/score/all/list',
    method: 'get',
    params: query
  })
}

// 查询详情
export function showTip(query) {
  return request({
    url: '/stucms/score/all/showDetailTips',
    method: 'get',
    params: query
  })
}

// 查询课程饼图
export function showCoursePie(query) {
  return request({
    url: '/stucms/score/all/showCoursePie',
    method: 'get',
    params: query
  })
}

