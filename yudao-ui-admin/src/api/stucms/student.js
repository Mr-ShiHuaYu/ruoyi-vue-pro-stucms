import request from '@/utils/request'

// 创建学生管理
export function createStudent(data) {
  return request({
    url: '/stucms/student/create',
    method: 'post',
    data: data
  })
}

// 更新学生管理
export function updateStudent(data) {
  return request({
    url: '/stucms/student/update',
    method: 'put',
    data: data
  })
}

// 删除学生管理
export function deleteStudent(id) {
  return request({
    url: '/stucms/student/delete?id=' + id,
    method: 'delete'
  })
}

// 获得学生管理
export function getStudent(id) {
  return request({
    url: '/stucms/student/get?id=' + id,
    method: 'get'
  })
}


// 获取简单学生列表
export function getSimpleStudentList(query) {
  return request({
    url: '/stucms/student/simple-page',
    method: 'get',
    params: query
  })
}


// 获得学生管理分页
export function getStudentPage(query) {
  return request({
    url: '/stucms/student/page',
    method: 'get',
    params: query
  })
}

// 导出学生管理 Excel
export function exportStudentExcel(query) {
  return request({
    url: '/stucms/student/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
