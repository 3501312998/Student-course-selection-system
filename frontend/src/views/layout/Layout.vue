<!-- 主布局组件（导航栏 + 侧边菜单 + 内容区） -->
<template>
  <div class="layout-container">
    <header class="layout-header">
      <div class="logo">
        <el-icon><Notebook /></el-icon>
        学生选课系统
      </div>
      <div class="header-actions">
        <span>欢迎，{{ userStore.user?.realName || userStore.user?.username }}</span>
        <el-button text style="color: #fff" @click="goProfile">
          <el-icon><User /></el-icon> 个人信息
        </el-button>
        <el-button text style="color: #fff" @click="handleLogout">
          <el-icon><CircleClose /></el-icon> 退出登录
        </el-button>
      </div>
    </header>

    <div style="display: flex; margin-top: 60px; min-height: calc(100vh - 60px);">
      <el-menu
        :default-active="activeMenu"
        router
        style="width: 220px; min-height: 100%; border-right: 1px solid #e4e7ed;"
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-menu-item index="/courses">
          <el-icon><Document /></el-icon>
          <span>课程列表</span>
        </el-menu-item>

        <el-menu-item v-if="userStore.isStudent" index="/schedule">
          <el-icon><Clock /></el-icon>
          <span>我的课表</span>
        </el-menu-item>

        <el-menu-item v-if="userStore.isStudent" index="/grades">
          <el-icon><View /></el-icon>
          <span>成绩查询</span>
        </el-menu-item>

        <el-sub-menu v-if="userStore.isTeacher" index="teacher">
          <template #title>
            <el-icon><Edit /></el-icon>
            <span>教学管理</span>
          </template>
          <el-menu-item index="/teacher-courses">
            <el-icon><Document /></el-icon>
            <span>我的课程</span>
          </el-menu-item>
          <el-menu-item index="/teacher/grades">
            <el-icon><View /></el-icon>
            <span>成绩管理</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu v-if="userStore.isAdmin" index="admin">
          <template #title>
            <el-icon><Avatar /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/admin/users">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/courses">
            <el-icon><Document /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>

      <main class="layout-main" style="flex: 1; padding: 24px; background: #f5f7fa;">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 当前激活的菜单项，由路由路径决定
const activeMenu = computed(() => route.path)

/** 跳转到个人信息页 */
function goProfile() {
  router.push('/profile')  // 跳转
}

/** 退出登录：弹出确认框 -> 清除认证信息 -> 跳转登录页 */
function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {  // 确认弹窗
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    userStore.logout()
    router.push('/login')
  }).catch(() => {})
}
</script>
