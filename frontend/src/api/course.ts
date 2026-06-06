import request from '@/utils/request'
import type { ApiResult, Course, PageResult } from '@/types'

export function getCourses(params: { page: number; size: number; keyword?: string; semester?: string }) {
  return request.get<any, ApiResult<PageResult<Course>>>('/courses', { params })
}

export function getCourseDetail(id: number) {
  return request.get<any, ApiResult<Course>>(`/courses/${id}`)
}

export function createCourse(data: Partial<Course>) {
  return request.post<any, ApiResult<Course>>('/teacher/courses', data)
}

export function updateCourse(id: number, data: Partial<Course>) {
  return request.put<any, ApiResult<Course>>(`/teacher/courses/${id}`, data)
}

export function deleteCourse(id: number) {
  return request.delete<any, ApiResult<null>>(`/courses/${id}`)
}

export function toggleCourseStatus(id: number) {
  return request.put<any, ApiResult<null>>(`/courses/${id}/toggle-status`)
}
