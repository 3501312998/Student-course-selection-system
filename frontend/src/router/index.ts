// 路由配置
// 定义所有前端页面路由，配置路由守卫实现登录拦截
import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/storage'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/user/Login.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/user/Register.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/',
      component: () => import('@/views/layout/Layout.vue'),
      meta: { requiresAuth: true },
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'Home',
          component: () => import('@/views/layout/Home.vue'),
          meta: { title: '首页' },
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
          meta: { title: '我的课表', roles: ['STUDENT'] },
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
          meta: { title: '我的课程', roles: ['TEACHER'] },
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
          meta: { title: '用户管理', roles: ['ADMIN'] },
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

// 404 通配路由
router.addRoute({
  path: '/:pathMatch(.*)*',
  name: 'NotFound',
  component: () => import('@/views/layout/NotFound.vue'),
  meta: { requiresAuth: false },
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = getToken()

  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
    return
  }

  if ((to.path === '/login' || to.path === '/register') && token) {
    next('/home')
    return
  }

  next()
})

export default router
