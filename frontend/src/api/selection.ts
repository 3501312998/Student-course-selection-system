// 选课 API
// 封装选课、退课、课表、成绩、学生名单等后端接口调用
import request from '@/utils/request'
import type { ApiResult } from '@/types'

export function selectCourse(courseId: number) {
  return request.post<any, ApiResult<null>>(`/selection/select/${courseId}`)
}

export function dropCourse(courseId: number) {
  return request.post<any, ApiResult<null>>(`/selection/drop/${courseId}`)
}

export function getSchedule() {
  return request.get<any, ApiResult<any[]>>('/selection/schedule')
}

export function getGrades() {
  return request.get<any, ApiResult<any[]>>('/selection/grades')
}

export function getMyCourses() {
  return request.get<any, ApiResult<any[]>>('/selection/my-courses')
}

export function getTeacherCourses() {
  return request.get<any, ApiResult<any[]>>('/teacher/courses')
}

export function getCourseStudents(courseId: number) {
  return request.get<any, ApiResult<any[]>>(`/teacher/courses/${courseId}/students`)
}

export function setScore(data: { studentId: number; courseId: number; score: number }) {
  return request.post<any, ApiResult<null>>('/teacher/score', data)
}
