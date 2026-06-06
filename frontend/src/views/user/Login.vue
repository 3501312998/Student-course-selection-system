<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2><el-icon style="vertical-align: middle;"><User /></el-icon> 学生选课系统 - 登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" size="large" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 100%;" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
        <div style="text-align: center;">
          还没有账号？
          <el-link type="primary" @click="router.push('/register')">立即注册</el-link>
        </div>
      </el-form>

      <el-divider>测试账号</el-divider>
      <div style="text-align: center; font-size: 13px; color: #909399;">
        <p>管理员: admin / 20040203</p>
        <p>教师: zhang_san / 20040203</p>
        <p>学生: stu_001 / 20040203</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref()

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login(form)
    const { token, userId, username, realName, role } = res.data
    userStore.loginSuccess({
      token,
      user: { id: userId, username, realName, role, status: 1 } as any,
    })
    ElMessage.success('登录成功')
    router.push('/home')
  } catch {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>
