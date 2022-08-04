import request from '@/utils/request'

// 创建课程
export function createCourse(data) {
  return request({
    url: '/stucms/course/create',
    method: 'post',
    data: data
  })
}

// 更新课程
export function updateCourse(data) {
  return request({
    url: '/stucms/course/update',
    method: 'put',
    data: data
  })
}

// 删除课程
export function deleteCourse(id) {
  return request({
    url: '/stucms/course/delete?id=' + id,
    method: 'delete'
  })
}

// 获得课程
export function getCourse(id) {
  return request({
    url: '/stucms/course/get?id=' + id,
    method: 'get'
  })
}

// 获得课程分页
export function getCoursePage(query) {
  return request({
    url: '/stucms/course/page',
    method: 'get',
    params: query
  })
}

// 获得课程分页
export function getCourseNameList() {
  return request({
    url: '/stucms/course/name-list',
    method: 'get'
  })
}

// 导出课程 Excel
export function exportCourseExcel(query) {
  return request({
    url: '/stucms/course/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
