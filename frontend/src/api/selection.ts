// 选课 API
// 封装选课、退课、课表、成绩、学生名单等后端接口调用
import request from '@/utils/request'
import type { ApiResult } from '@/types'

/** 选课 */
export function selectCourse(courseId: number) {
  return request.post<any, ApiResult<null>>(`/selection/select/${courseId}`)  // POST 选课
}

/** 退课 */
export function dropCourse(courseId: number) {
  return request.post<any, ApiResult<null>>(`/selection/drop/${courseId}`)  // POST 退课
}

/** 获取学生课表 */
export function getSchedule() {
  return request.get<any, ApiResult<any[]>>('/selection/schedule')  // GET 课表数据
}

/** 获取学生成绩 */
export function getGrades() {
  return request.get<any, ApiResult<any[]>>('/selection/grades')  // GET 成绩数据
}

/** 获取我的课程列表（按角色区分） */
export function getMyCourses() {
  return request.get<any, ApiResult<any[]>>('/selection/my-courses')  // GET 我的课程
}

/** 获取教师的授课课程列表 */
export function getTeacherCourses() {
  return request.get<any, ApiResult<any[]>>('/teacher/courses')  // GET 教师课程
}

/** 获取某课程的学生名单（含成绩） */
export function getCourseStudents(courseId: number) {
  return request.get<any, ApiResult<any[]>>(`/teacher/courses/${courseId}/students`)  // GET 学生名单
}

/** 录入/修改学生成绩 */
export function setScore(data: { studentId: number; courseId: number; score: number }) {
  return request.post<any, ApiResult<null>>('/teacher/score', data)  // POST 成绩数据
}
