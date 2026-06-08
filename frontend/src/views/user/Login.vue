<!-- 登录页面 -->
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

// 登录表单数据
const form = reactive({
  username: '',  // 用户名
  password: '',  // 密码
})

// 表单校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],  // 用户名必填
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],  // 密码必填
}

/** 处理登录：校验表单 -> 调用登录 API -> 保存用户信息 -> 跳转首页 */
async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)  // 校验表单
  if (!valid) return  // 校验不通过则停止

  loading.value = true
  try {
    const res = await login(form)  // 调用登录接口
    const { token, userId, username, realName, role } = res.data
    // 保存 Token 和用户信息到 Store 和本地存储
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
