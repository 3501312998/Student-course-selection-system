// 管理员 API
// 封装用户管理、课程管理等后台管理接口调用
import request from '@/utils/request'
import type { ApiResult, UserInfo } from '@/types'

/** 获取用户列表（可选按角色筛选） */
export function getUsers(role?: string) {
  return request.get<any, ApiResult<UserInfo[]>>('/admin/users', { params: { role } })  // GET 用户列表
}

/** 启用/禁用用户 */
export function toggleUserStatus(id: number) {
  return request.put<any, ApiResult<null>>(`/admin/users/${id}/toggle-status`)  // PUT 切换状态
}
