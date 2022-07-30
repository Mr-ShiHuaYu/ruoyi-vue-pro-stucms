import request from '@/utils/request'

// 查询成绩列表
export function listAllScore(query) {
    return request({
        url: '/stucms/score_all/list',
        method: 'get',
        params: query
    })
}

// 查询详情
export function showTip(data) {
    return request({
        url: '/stucms/score_all/showDetailTips',
        method: 'post',
        data: data
    })
}

// 查询课程饼图
export function showCoursePie(data) {
    return request({
        url: '/stucms/score_all/showCoursePie',
        method: 'post',
        data: data
    })
}

