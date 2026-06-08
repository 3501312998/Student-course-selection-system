// 类型定义
// 定义项目中使用的 TypeScript 接口类型，确保前后端数据格式一致
// 用户信息
export interface UserInfo {
  id: number
  username: string
  realName: string
  email?: string
  phone?: string
  role: 'ADMIN' | 'TEACHER' | 'STUDENT'
  gender?: number
  studentNo?: string
  status: number
  createTime?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  tokenType: string
  userId: number
  username: string
  realName: string
  role: string
}

// 课程
export interface Course {
  id: number
  courseName: string
  courseCode: string
  teacherId: number
  credit: number
  maxCapacity: number
  currentCount: number
  semester: string
  schedule: string
  classroom: string
  description?: string
  status: number
}

// 选课记录
export interface CourseSelection {
  id: number
  studentId: number
  courseId: number
  status: number
  score?: number
  selectTime: string
}

// 分页响应
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// API 统一响应
export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 首页统计数据
export interface DashboardStats {
  // 学生相关
  selectedCourseCount?: number
  totalCredits?: number
  totalCourses?: number
  // 教师相关
  courseCount?: number
  studentCount?: number
  ungradedCount?: number
  // 管理员相关
  totalUsers?: number
  teacherCount?: number
  adminCount?: number
  totalSelections?: number
}
