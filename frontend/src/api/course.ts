// 课程 API
// 封装课程列表查询、详情查看、创建更新等后端接口调用
import request from '@/utils/request'
import type { ApiResult, Course, PageResult } from '@/types'

/** 获取课程分页列表（公开接口） */
export function getCourses(params: { page: number; size: number; keyword?: string; semester?: string }) {
  return request.get<any, ApiResult<PageResult<Course>>>('/courses', { params })  // GET 分页查询
}

/** 获取课程详情 */
export function getCourseDetail(id: number) {
  return request.get<any, ApiResult<Course>>(`/courses/${id}`)  // GET 根据 ID 查询
}

/** 创建课程（教师） */
export function createCourse(data: Partial<Course>) {
  return request.post<any, ApiResult<Course>>('/teacher/courses', data)  // POST 新建课程
}

/** 更新课程信息 */
export function updateCourse(id: number, data: Partial<Course>) {
  return request.put<any, ApiResult<Course>>(`/teacher/courses/${id}`, data)  // PUT 更新
}

/** 删除课程 */
export function deleteCourse(id: number) {
  return request.delete<any, ApiResult<null>>(`/courses/${id}`)  // DELETE 删除
}

/** 切换课程开放/关闭状态 */
export function toggleCourseStatus(id: number) {
  return request.put<any, ApiResult<null>>(`/courses/${id}/toggle-status`)  // PUT 切换状态
}
