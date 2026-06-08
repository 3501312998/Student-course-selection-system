<!-- 管理员用户管理页（启用/禁用用户） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><UserFilled /></el-icon> 用户管理
    </div>

    <el-card shadow="never">
      <div class="table-toolbar">
        <el-select v-model="roleFilter" placeholder="筛选角色" clearable style="width: 150px;" @change="loadUsers">
          <el-option label="全部" value="" />
          <el-option label="管理员" value="ADMIN" />
          <el-option label="教师" value="TEACHER" />
          <el-option label="学生" value="STUDENT" />
        </el-select>
      </div>

      <el-table :data="users" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="studentNo" label="学号/工号" width="120" />
        <el-table-column label="角色" width="80">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'TEACHER' ? 'warning' : 'primary'" size="small">
              {{ row.role === 'ADMIN' ? '管理员' : row.role === 'TEACHER' ? '教师' : '学生' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="160" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              :type="row.status === 1 ? 'warning' : 'success'"
              link
              @click="handleToggle(row.id)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUsers, toggleUserStatus } from '@/api/admin'
import type { UserInfo } from '@/types'

const users = ref<UserInfo[]>([])
const loading = ref(false)
const roleFilter = ref('')

onMounted(() => loadUsers())  // 组件挂载后加载用户

/** 获取用户列表（可筛选角色） */
async function loadUsers() {
  loading.value = true
  try {
    const res = await getUsers(roleFilter.value || undefined)  // 调用用户列表接口
    users.value = res.data  // 保存用户数据
  } finally {
    loading.value = false
  }
}

/** 切换用户启用/禁用状态 */
async function handleToggle(id: number) {
  try {
    await ElMessageBox.confirm('确定要切换用户状态吗？', '提示', { type: 'warning' })  // 确认弹窗
    await toggleUserStatus(id)  // 调用切换状态接口
    ElMessage.success('操作成功')
    loadUsers()
  } catch { /* cancelled or error */ }
}
</script>
