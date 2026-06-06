import request from '@/utils/request'
import type { ApiResult, LoginResponse, UserInfo } from '@/types'

export function login(data: { username: string; password: string }) {
  return request.post<any, ApiResult<LoginResponse>>('/auth/login', data)
}

export function register(data: {
  username: string
  password: string
  realName: string
  email?: string
  phone?: string
  studentNo: string
}) {
  return request.post<any, ApiResult<null>>('/auth/register', data)
}

export function getCurrentUser() {
  return request.get<any, ApiResult<UserInfo>>('/auth/me')
}

export function updateProfile(data: { realName?: string; email?: string; phone?: string; gender?: number }) {
  return request.put<any, ApiResult<null>>('/auth/profile', data)
}

export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return request.put<any, ApiResult<null>>('/auth/password', data)
}
