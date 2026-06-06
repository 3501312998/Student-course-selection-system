<template>
  <div class="page-container">
    <el-row :gutter="24">
      <el-col :span="16">
        <el-card shadow="never">
          <h2 style="margin-bottom: 16px;">欢迎使用学生选课系统</h2>
          <p style="color: #606266; line-height: 1.8; font-size: 15px;">
            本系统提供在线选课、课程管理、成绩查询等功能。<br/>
            请根据您的角色使用对应的功能模块。
          </p>
          <el-divider />
          <el-row :gutter="16">
            <el-col :span="8" v-for="item in features" :key="item.title">
              <el-card shadow="hover" style="margin-bottom: 16px; text-align: center; cursor: pointer;" @click="router.push(item.path)">
                <el-icon :size="32" :color="item.color">
                  <component :is="item.icon" />
                </el-icon>
                <p style="margin-top: 8px; font-weight: 600;">{{ item.title }}</p>
                <p style="font-size: 13px; color: #909399;">{{ item.desc }}</p>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <span><el-icon><InfoFilled /></el-icon> 个人信息</span>
          </template>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="用户名">{{ userStore.user?.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ userStore.user?.realName }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ roleMap[userStore.user?.role || ''] }}</el-descriptions-item>
            <el-descriptions-item label="学号/工号">{{ userStore.user?.studentNo || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const roleMap: Record<string, string> = {
  ADMIN: '管理员',
  TEACHER: '教师',
  STUDENT: '学生',
}

const features = [
  { title: '浏览课程', desc: '查看所有可选课程', icon: 'Document', path: '/courses', color: '#409eff' },
  { title: '我的课表', desc: '查看已选课程', icon: 'Clock', path: '/schedule', color: '#67c23a' },
  { title: '成绩查询', desc: '查看课程成绩', icon: 'View', path: '/grades', color: '#e6a23c' },
]
</script>
