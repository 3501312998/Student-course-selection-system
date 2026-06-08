// 管理员 API
// 封装用户管理、课程管理等后台管理接口调用
import request from '@/utils/request'
import type { ApiResult, UserInfo } from '@/types'

export function getUsers(role?: string) {
  return request.get<any, ApiResult<UserInfo[]>>('/admin/users', { params: { role } })
}

export function toggleUserStatus(id: number) {
  return request.put<any, ApiResult<null>>(`/admin/users/${id}/toggle-status`)
}
