<!-- 首页 - 紧凑仪表盘，一屏铺满 -->
<template>
  <div class="home-container">
    <!-- 1. 顶部：欢迎横幅（窄） -->
    <div class="home-banner">
      <div class="home-banner-bg">
        <span v-for="n in 12" :key="n" class="particle" :style="{ '--i': n }"></span>
      </div>
      <!-- 3D 装饰环 -->
      <div class="home-3d-ring">
        <div class="ring"><div class="ring-inner"></div></div>
      </div>
      <div class="home-banner-text">
        <h3>{{ adminTitle }}</h3>
        <span>{{ welcomeMessage }}</span>
      </div>
      <el-avatar :size="48" :style="{ background: roleColor }">{{ roleAvatarText }}</el-avatar>
    </div>

    <!-- 2. 中部：统计卡 + 个人信息（一行铺满） -->
    <div class="home-mid-row">
      <div
        class="home-stat-item"
        v-for="s in statCards"
        :key="s.label"
        @click="s.path ? router.push(s.path) : null"
        :style="{ cursor: s.path ? 'pointer' : 'default' }"
      >
        <div class="home-stat-value">{{ s.value }}</div>
        <div class="home-stat-label">{{ s.label }}</div>
        <el-icon :size="28" :color="s.color" class="home-stat-icon">
          <component :is="s.icon" />
        </el-icon>
      </div>
      <div class="home-profile-slim">
        <el-avatar :size="40" :style="{ background: roleColor }">{{ roleAvatarText }}</el-avatar>
        <div class="home-profile-info">
          <strong>{{ userStore.user?.realName || userStore.user?.username }}</strong>
          <el-tag :type="roleTagType" size="small">{{ roleLabel }}</el-tag>
        </div>
        <div class="home-profile-meta">
          <span>{{ userStore.user?.studentNo || userStore.user?.email || '-' }}</span>
        </div>
        <el-button size="small" text @click="router.push('/profile')">编辑</el-button>
      </div>
    </div>

    <!-- 3. 底部：功能卡片 + 课表（弹性填满） -->
    <div class="home-bottom">
      <div class="home-features">
        <div
          class="home-feat-item"
          v-for="item in features"
          :key="item.title"
          @click="router.push(item.path)"
        >
          <el-icon :size="22" :color="item.color"><component :is="item.icon" /></el-icon>
          <span class="home-feat-title">{{ item.title }}</span>
        </div>
      </div>
      <div class="home-table-wrap" v-if="recentList.length > 0">
        <div class="home-table-header">
          <el-icon><Clock /></el-icon> {{ recentTitle }}
        </div>
        <el-table :data="recentList" size="small" stripe height="100%" style="width:100%">
          <el-table-column prop="courseName" label="课程" min-width="120" />
          <el-table-column prop="schedule" label="时间" width="130" v-if="userStore.isStudent" />
          <el-table-column prop="classroom" label="地点" width="100" v-if="userStore.isStudent" />
          <el-table-column prop="studentCount" label="人数" width="70" v-if="userStore.isTeacher" />
          <el-table-column prop="status" label="状态" width="70">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                {{ row.status === 1 ? '开放' : '关闭' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 管理员：系统概览面板 -->
      <div class="home-admin-panel" v-if="userStore.isAdmin">
        <el-row :gutter="16" style="height:100%">
          <!-- 左侧：系统数据概览 -->
          <el-col :span="14" style="height:100%">
            <div class="admin-section-card admin-overview">
              <div class="admin-section-title">
                <el-icon><DataBoard /></el-icon> 系统数据概览
              </div>
              <div class="overview-grid">
                <div class="ov-item">
                  <div class="ov-icon" style="background:#ecf5ff;color:#409eff"><el-icon :size="24"><User /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.totalUsers ?? '-' }}</div>
                    <div class="ov-label">用户总数</div>
                  </div>
                </div>
                <div class="ov-item">
                  <div class="ov-icon" style="background:#fef0f0;color:#f56c6c"><el-icon :size="24"><Avatar /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.adminCount ?? '-' }}</div>
                    <div class="ov-label">管理员</div>
                  </div>
                </div>
                <div class="ov-item">
                  <div class="ov-icon" style="background:#fdf6ec;color:#e6a23c"><el-icon :size="24"><Edit /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.teacherCount ?? '-' }}</div>
                    <div class="ov-label">教师</div>
                  </div>
                </div>
                <div class="ov-item">
                  <div class="ov-icon" style="background:#f0f9eb;color:#67c23a"><el-icon :size="24"><UserFilled /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.studentCount ?? '-' }}</div>
                    <div class="ov-label">学生</div>
                  </div>
                </div>
                <div class="ov-item">
                  <div class="ov-icon" style="background:#ecf5ff;color:#409eff"><el-icon :size="24"><Document /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.totalCourses ?? '-' }}</div>
                    <div class="ov-label">课程总数</div>
                  </div>
                </div>
                <div class="ov-item">
                  <div class="ov-icon" style="background:#f5f0ff;color:#722ed1"><el-icon :size="24"><CircleCheck /></el-icon></div>
                  <div>
                    <div class="ov-value">{{ stats.totalSelections ?? '-' }}</div>
                    <div class="ov-label">选课记录</div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <!-- 右侧：系统状态 + 快捷操作 -->
          <el-col :span="10" style="height:100%;display:flex;flex-direction:column;gap:12px;">
            <div class="admin-section-card" style="flex:1;">
              <div class="admin-section-title">
                <el-icon><InfoFilled /></el-icon> 系统状态
              </div>
              <div class="sys-status">
                <div class="sys-row">
                  <span class="sys-label">运行状态</span>
                  <el-tag size="small" type="success">● 正常</el-tag>
                </div>
                <div class="sys-row">
                  <span class="sys-label">数据库</span>
                  <el-tag size="small" type="success">● 已连接</el-tag>
                </div>
                <div class="sys-row">
                  <span class="sys-label">接口服务</span>
                  <el-tag size="small" type="success">● 运行中</el-tag>
                </div>
                <div class="sys-row">
                  <span class="sys-label">用户在线</span>
                  <span style="font-weight:600;">-</span>
                </div>
                <div class="sys-row">
                  <span class="sys-label">今日访问</span>
                  <span style="font-weight:600;">-</span>
                </div>
              </div>
            </div>
            <div class="admin-section-card" style="flex:1;">
              <div class="admin-section-title">
                <el-icon><Grid /></el-icon> 快捷操作
              </div>
              <div style="display:flex;flex-wrap:wrap;gap:8px;padding:4px 0;">
                <el-button size="small" type="primary" @click="router.push('/admin/users')">
                  <el-icon><UserFilled /></el-icon> 用户管理
                </el-button>
                <el-button size="small" type="warning" @click="router.push('/admin/courses')">
                  <el-icon><Document /></el-icon> 课程管理
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getDashboardStats } from '@/api/dashboard'
import { getSchedule, getTeacherCourses } from '@/api/selection'
import type { Course } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const roleMap: Record<string, string> = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }
const roleLabel = computed(() => roleMap[userStore.role] || '')
const roleTagType = computed(() => ({ ADMIN: 'danger', TEACHER: 'warning', STUDENT: 'primary' }[userStore.role] || ''))
const roleColor = computed(() => ({ ADMIN: '#f56c6c', TEACHER: '#e6a23c', STUDENT: '#409eff' }[userStore.role] || '#909399'))
const roleAvatarText = computed(() => ({ ADMIN: '管', TEACHER: '教', STUDENT: '学' }[userStore.role] || '?'))

// 根据当前时间生成时段问候语
const adminTitle = computed(() => {
  const name = userStore.user?.realName || userStore.user?.username || ''
  const hour = new Date().getHours()
  const greeting = hour < 6 ? '夜深了' : hour < 12 ? '上午好' : hour < 14 ? '中午好' : hour < 18 ? '下午好' : '晚上好'  // 时段判断
  return `${greeting}，${name}`
})

// 根据角色生成欢迎语
const welcomeMessage = computed(() => {
  if (userStore.isStudent) return '浏览课程、查看课表、查询成绩，轻松管理学习计划。'  // 学生欢迎语
  if (userStore.isTeacher) return '管理课程、学生名单、录入成绩，教学管理一站式。'  // 教师欢迎语
  if (userStore.isAdmin) return '管理系统用户、监控课程运行、维护系统数据。'  // 管理员欢迎语
  return '欢迎使用学生选课系统。'
})

const stats = ref<Record<string, number>>({})

// 按角色生成统计卡片数据
const statCards = computed(() => {
  const base = { path: '' } as any
  if (userStore.isStudent) return [  // 学生统计卡片
    { label: '已选课程', value: stats.value.selectedCourseCount ?? '0', icon: 'Clock', color: '#409eff', path: '/schedule' },
    { label: '总学分', value: stats.value.totalCredits ?? '0', icon: 'Reading', color: '#67c23a' },
    { label: '可选课程', value: stats.value.totalCourses ?? '0', icon: 'Document', color: '#e6a23c', path: '/courses' },
  ].map(i => ({ ...base, ...i }))
  if (userStore.isTeacher) return [  // 教师统计卡片
    { label: '授课课程', value: stats.value.courseCount ?? '0', icon: 'Edit', color: '#409eff', path: '/teacher-courses' },
    { label: '选课学生', value: stats.value.studentCount ?? '0', icon: 'UserFilled', color: '#67c23a' },
    { label: '待录成绩', value: stats.value.ungradedCount ?? '0', icon: 'Warning', color: '#e6a23c', path: '/teacher/grades' },
  ].map(i => ({ ...base, ...i }))
  if (userStore.isAdmin) return [  // 管理员统计卡片
    { label: '用户', value: stats.value.totalUsers ?? '0', icon: 'User', color: '#409eff', path: '/admin/users' },
    { label: '课程', value: stats.value.totalCourses ?? '0', icon: 'Document', color: '#67c23a' },
    { label: '选课记录', value: stats.value.totalSelections ?? '0', icon: 'DataBoard', color: '#e6a23c' },
  ]
  return []
})

const allFeatures = [
  { title: '浏览课程', icon: 'Document', path: '/courses', color: '#409eff', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
  { title: '我的课表', icon: 'Clock', path: '/schedule', color: '#67c23a', roles: ['STUDENT'] },
  { title: '我的课程', icon: 'Edit', path: '/teacher-courses', color: '#e6a23c', roles: ['TEACHER'] },
  { title: '成绩查询', icon: 'View', path: '/grades', color: '#e6a23c', roles: ['STUDENT'] },
  { title: '用户管理', icon: 'UserFilled', path: '/admin/users', color: '#f56c6c', roles: ['ADMIN'] },
  { title: '课程管理', icon: 'DataBoard', path: '/admin/courses', color: '#f56c6c', roles: ['ADMIN'] },
  { title: '成绩管理', icon: 'Edit', path: '/teacher/grades', color: '#f56c6c', roles: ['TEACHER'] },
]

// 按角色筛选可用的快捷功能
const features = computed(() =>
  allFeatures.filter((item) => item.roles.includes(userStore.role))  // 过滤当前角色可见的功能
)

const recentList = ref<any[]>([])
const recentTitle = ref('')

onMounted(async () => {  // 组件挂载后加载数据和统计
  try {
    const res = await getDashboardStats()  // 获取仪表盘统计数据
    stats.value = res.data as Record<string, number>
  } catch { /* handled */ }
  try {
    if (userStore.isStudent) {  // 学生加载课表
      const res = await getSchedule()  // 获取课表
      recentList.value = (res.data || []).slice(0, 5)
      recentTitle.value = '我的课表'
    } else if (userStore.isTeacher) {  // 教师加载课程
      const res = await getTeacherCourses()  // 获取课程列表
      recentList.value = (res.data || []).slice(0, 5).map((c: Course) => ({ ...c, studentCount: c.currentCount }))
      recentTitle.value = '我的课程'
    }
  } catch { /* handled */ }
})
</script>
