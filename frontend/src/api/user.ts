// 用户认证 API
// 封装登录、注册、获取用户信息、更新资料等后端接口调用
import request from '@/utils/request'
import type { ApiResult, LoginResponse, UserInfo } from '@/types'

/** 用户登录 */
export function login(data: { username: string; password: string }) {
  return request.post<any, ApiResult<LoginResponse>>('/auth/login', data)  // POST 登录信息
}

/** 用户注册 */
export function register(data: {
  username: string
  password: string
  realName: string
  email?: string
  phone?: string
  studentNo: string
}) {
  return request.post<any, ApiResult<null>>('/auth/register', data)  // POST 注册信息
}

/** 获取当前登录用户信息 */
export function getCurrentUser() {
  return request.get<any, ApiResult<UserInfo>>('/auth/me')  // GET 请求
}

/** 更新个人信息 */
export function updateProfile(data: { realName?: string; email?: string; phone?: string; gender?: number }) {
  return request.put<any, ApiResult<null>>('/auth/profile', data)  // PUT 请求更新
}

/** 修改密码 */
export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return request.put<any, ApiResult<null>>('/auth/password', data)  // PUT 新密码
}
