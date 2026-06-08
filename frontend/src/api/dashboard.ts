// 首页仪表盘 API
// 封装首页统计数据接口调用
import request from '@/utils/request'
import type { ApiResult, DashboardStats } from '@/types'

/** 获取首页仪表盘统计数据（按角色返回不同字段） */
export function getDashboardStats() {
  return request.get<any, ApiResult<DashboardStats>>('/dashboard/stats')  // GET 统计数据
}
