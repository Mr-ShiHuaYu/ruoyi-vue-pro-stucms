import request from '@/utils/request'

// 创建考试
export function createExam(data) {
  return request({
    url: '/stucms/exam/create',
    method: 'post',
    data: data
  })
}

// 更新考试
export function updateExam(data) {
  return request({
    url: '/stucms/exam/update',
    method: 'put',
    data: data
  })
}

// 删除考试
export function deleteExam(id) {
  return request({
    url: '/stucms/exam/delete?id=' + id,
    method: 'delete'
  })
}

// 获得考试
export function getExam(id) {
  return request({
    url: '/stucms/exam/get?id=' + id,
    method: 'get'
  })
}

// 获得考试分页
export function getExamPage(query) {
  return request({
    url: '/stucms/exam/page',
    method: 'get',
    params: query
  })
}

// 导出考试 Excel
export function exportExamExcel(query) {
  return request({
    url: '/stucms/exam/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
