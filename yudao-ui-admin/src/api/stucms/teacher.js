import request from '@/utils/request'

// 创建老师
export function createTeacher(data) {
  return request({
    url: '/stucms/teacher/create',
    method: 'post',
    data: data
  })
}

// 更新老师
export function updateTeacher(data) {
  return request({
    url: '/stucms/teacher/update',
    method: 'put',
    data: data
  })
}

// 删除老师
export function deleteTeacher(id) {
  return request({
    url: '/stucms/teacher/delete?id=' + id,
    method: 'delete'
  })
}

// 获得老师
export function getTeacher(id) {
  return request({
    url: '/stucms/teacher/get?id=' + id,
    method: 'get'
  })
}

// 获得老师分页
export function getTeacherPage(query) {
  return request({
    url: '/stucms/teacher/page',
    method: 'get',
    params: query
  })
}

// 获得老师全部列表
export function getTeacherList() {
  return request({
    url: '/stucms/teacher/list-all-simple',
    method: 'get'
  })
}

// 导出老师 Excel
export function exportTeacherExcel(query) {
  return request({
    url: '/stucms/teacher/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
