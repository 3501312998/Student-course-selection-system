// 用户状态管理（Pinia Store）
// 管理登录状态、用户信息、角色判断，提供登录/登出/刷新用户信息等操作方法
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser } from '@/api/user'
import { setToken, setUser, getToken, getUser, clearAuth } from '@/utils/storage'
import type { UserInfo } from '@/types'

export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(getUser())
  const token = ref<string | null>(getToken())

  const isLoggedIn = computed(() => !!token.value)
  const role = computed(() => user.value?.role || '')
  const isAdmin = computed(() => role.value === 'ADMIN')
  const isTeacher = computed(() => role.value === 'TEACHER')
  const isStudent = computed(() => role.value === 'STUDENT')

  function loginSuccess(data: { token: string; user: UserInfo }) {
    token.value = data.token
    user.value = data.user
    setToken(data.token)
    setUser(data.user)
  }

  function logout() {
    token.value = null
    user.value = null
    clearAuth()
  }

  async function fetchUserInfo() {
    try {
      const res = await getCurrentUser()
      user.value = res.data
      setUser(res.data)
    } catch {
      logout()
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
