// 路由配置
// 定义所有前端页面路由，配置路由守卫实现登录拦截
import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/storage'

// 创建 Vue Router 实例，使用 HTML5 History 模式
const router = createRouter({
  history: createWebHistory(),  // HTML5 History 模式，无 # 号
  routes: [  // 路由表
    {
      path: '/login',  // 登录页
      name: 'Login',
      component: () => import('@/views/user/Login.vue'),
      meta: { requiresAuth: false },  // 无需登录即可访问
    },
    {
      path: '/register',  // 注册页
      name: 'Register',
      component: () => import('@/views/user/Register.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/',  // 主布局，包裹所有需登录的页面
      component: () => import('@/views/layout/Layout.vue'),
      meta: { requiresAuth: true },  // 需要登录才能访问
      redirect: '/home',  // 默认重定向到首页
      children: [  // 子路由，在 Layout 的 <router-view> 中渲染
        {
          path: 'home',
          name: 'Home',
          component: () => import('@/views/layout/Home.vue'),
          meta: { title: '首页' },  // 页面标题
        },
        {
          path: 'courses',
          name: 'Courses',
          component: () => import('@/views/course/CourseList.vue'),
          meta: { title: '课程列表' },
        },
        {
          path: 'courses/:id',
          name: 'CourseDetail',
          component: () => import('@/views/course/CourseDetail.vue'),
          meta: { title: '课程详情' },
        },
        {
          path: 'schedule',
          name: 'Schedule',
          component: () => import('@/views/course/Schedule.vue'),
          meta: { title: '我的课表', roles: ['STUDENT'] },  // 仅学生可见
        },
        {
          path: 'grades',
          name: 'Grades',
          component: () => import('@/views/grade/GradeList.vue'),
          meta: { title: '成绩查询' },
        },
        {
          path: 'teacher-courses',
          name: 'TeacherCourses',
          component: () => import('@/views/course/TeacherCourses.vue'),
          meta: { title: '我的课程', roles: ['TEACHER'] },  // 仅教师可见
        },
        {
          path: 'teacher-courses/:id/students',
          name: 'CourseStudents',
          component: () => import('@/views/grade/ScoreEntry.vue'),
          meta: { title: '学生名单', roles: ['TEACHER'] },
        },
        {
          path: 'teacher/grades',
          name: 'TeacherGradeManage',
          component: () => import('@/views/grade/TeacherGradeManage.vue'),
          meta: { title: '成绩管理', roles: ['TEACHER'] },
        },
        {
          path: 'admin/users',
          name: 'AdminUsers',
          component: () => import('@/views/admin/UserManage.vue'),
          meta: { title: '用户管理', roles: ['ADMIN'] },  // 仅管理员可见
        },
        {
          path: 'admin/courses',
          name: 'AdminCourses',
          component: () => import('@/views/admin/CourseManage.vue'),
          meta: { title: '课程管理', roles: ['ADMIN'] },
        },
        {
          path: 'profile',
          name: 'Profile',
          component: () => import('@/views/user/Profile.vue'),
          meta: { title: '个人信息' },
        },
      ],
    },
  ],
})

// 404 通配路由：匹配所有未定义路径
router.addRoute({  // 动态添加 404 路由
  path: '/:pathMatch(.*)*',  // 通配所有未匹配路径
  name: 'NotFound',
  component: () => import('@/views/layout/NotFound.vue'),
  meta: { requiresAuth: false },
})

// 路由守卫：登录状态检查，未登录跳转登录页
router.beforeEach((to, _from, next) => {  // 全局前置守卫
  const token = getToken()  // 从本地存储获取 Token

  if (to.meta.requiresAuth !== false && !token) {  // 需登录但无 Token
    next('/login')  // 跳转到登录页
    return
  }

  if ((to.path === '/login' || to.path === '/register') && token) {  // 已登录时访问登录/注册页
    next('/home')  // 重定向到首页
    return
  }

  next()
})

export default router
