// 本地存储工具函数
// 封装 localStorage 操作，管理 Token 和用户信息的持久化
// 本地存储工具

const TOKEN_KEY = 'token'  // 本地存储中 Token 的键名
const USER_KEY = 'user'  // 本地存储中用户信息的键名

/** 获取本地存储的 JWT Token */
export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)  // 从 localStorage 读取 Token
}

/** 保存 JWT Token 到本地存储 */
export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)  // 写入 localStorage
}

/** 删除本地存储的 Token */
export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

/** 获取本地存储的用户信息 */
export function getUser(): any {
  const user = localStorage.getItem(USER_KEY)  // 读取用户 JSON 字符串
  return user ? JSON.parse(user) : null  // 反序列化为对象
}

/** 保存用户信息到本地存储 */
export function setUser(user: any): void {
  localStorage.setItem(USER_KEY, JSON.stringify(user))  // 序列化后存储
}

/** 删除本地存储的用户信息 */
export function removeUser(): void {
  localStorage.removeItem(USER_KEY)
}

/** 清除所有认证信息（Token + 用户信息） */
export function clearAuth(): void {
  removeToken()
  removeUser()
}
