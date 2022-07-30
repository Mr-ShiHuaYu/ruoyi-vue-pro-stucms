import request from '@/utils/request'

// 查询成绩列表
export function listScore(query) {
  return request({
    url: '/stucms/score/page',
    method: 'get',
    params: query
  })
}

// 修改成绩
export function updateScore(data) {
  return request({
    url: '/stucms/score/update',
    method: 'put',
    data: data
  })
}

// 导出 Excel
export function exportScoreExcel(query) {
  return request({
    url: '/stucms/score/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
