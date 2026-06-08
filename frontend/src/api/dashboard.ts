// 首页仪表盘 API
// 封装首页统计数据接口调用
import request from '@/utils/request'
import type { ApiResult, DashboardStats } from '@/types'

export function getDashboardStats() {
  return request.get<any, ApiResult<DashboardStats>>('/dashboard/stats')
}
