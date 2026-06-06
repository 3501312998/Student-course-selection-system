<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <span><el-icon><User /></el-icon> 个人信息</span>
      </template>

      <el-form :model="form" label-width="100px" size="large">
        <el-form-item label="用户名">
          <el-input :model-value="userStore.user?.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="学号/工号">
          <el-input :model-value="userStore.user?.studentNo" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="0">未知</el-radio>
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <template #header>
        <span><el-icon><CircleCheck /></el-icon> 修改密码</span>
      </template>
      <el-form :model="pwdForm" label-width="100px" size="large">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handlePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { updateProfile, updatePassword } from '@/api/user'

const userStore = useUserStore()

const form = reactive({
  realName: '',
  email: '',
  phone: '',
  gender: 0,
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
})

onMounted(() => {
  if (userStore.user) {
    form.realName = userStore.user.realName || ''
    form.email = userStore.user.email || ''
    form.phone = userStore.user.phone || ''
    form.gender = userStore.user.gender || 0
  }
})

async function handleUpdate() {
  try {
    await updateProfile(form)
    ElMessage.success('保存成功')
    userStore.fetchUserInfo()
  } catch { /* handled by interceptor */ }
}

async function handlePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请填写完整')
    return
  }
  try {
    await updatePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
    ElMessage.success('密码修改成功')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
  } catch { /* handled by interceptor */ }
}
</script>
