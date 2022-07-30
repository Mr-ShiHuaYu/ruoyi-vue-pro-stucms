import request from '@/utils/request'

// 查询成绩列表
export function listPersonalScore(query) {
    return request({
        url: '/stucms/score_personal/list',
        method: 'get',
        params: query
    })
}

// 查询图1
export function getPersonalScoreChart1(studentId) {
    return request({
        url: '/stucms/score_personal/chart1/' + studentId,
        method: 'get'
    })
}

// 查询图2
export function getPersonalScoreChart2(studentId) {
    return request({
        url: '/stucms/score_personal/chart2/' + studentId,
        method: 'get'
    })
}

// 查询图3
export function getPersonalScoreChart3(studentId) {
    return request({
        url: '/stucms/score_personal/chart3/' + studentId,
        method: 'get'
    })
}

// 查询图4
export function getPersonalScoreChart4(studentId) {
    return request({
        url: '/stucms/score_personal/chart4/' + studentId,
        method: 'get'
    })
}
