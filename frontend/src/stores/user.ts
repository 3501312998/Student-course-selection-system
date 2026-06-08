// 用户状态管理（Pinia Store）
// 管理登录状态、用户信息、角色判断，提供登录/登出/刷新用户信息等操作方法
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser } from '@/api/user'
import { setToken, setUser, getToken, getUser, clearAuth } from '@/utils/storage'
import type { UserInfo } from '@/types'

// 用户相关状态和操作
// 管理登录状态、用户信息、角色判断
export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(getUser())  // 用户信息，初始从 localStorage 恢复
  const token = ref<string | null>(getToken())  // JWT Token，初始从 localStorage 恢复

  const isLoggedIn = computed(() => !!token.value)  // 是否已登录
  const role = computed(() => user.value?.role || '')  // 当前用户角色
  const isAdmin = computed(() => role.value === 'ADMIN')  // 是否为管理员
  const isTeacher = computed(() => role.value === 'TEACHER')  // 是否为教师
  const isStudent = computed(() => role.value === 'STUDENT')  // 是否为学生

  /** 登录成功处理：保存 Token 和用户信息 */
  function loginSuccess(data: { token: string; user: UserInfo }) {
    token.value = data.token  // 更新内存中的 Token
    user.value = data.user  // 更新内存中的用户信息
    setToken(data.token)  // 持久化 Token
    setUser(data.user)  // 持久化用户信息
  }

  /** 退出登录：清除所有认证信息 */
  function logout() {
    token.value = null  // 清空内存中的 Token
    user.value = null  // 清空内存中的用户信息
    clearAuth()  // 清空本地存储
  }

  /** 从后端刷新用户信息 */
  async function fetchUserInfo() {
    try {
      const res = await getCurrentUser()  // 调用后端接口获取用户信息
      user.value = res.data  // 更新内存
      setUser(res.data)  // 同步到本地存储
    } catch {  // 请求失败（如 Token 过期）
      logout()  // 自动退出
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    role,
    isAdmin,
    isTeacher,
    isStudent,
    loginSuccess,
    logout,
    fetchUserInfo,
  }
})
