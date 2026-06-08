<!-- 课程列表页（分页搜索 + 选课入口） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Document /></el-icon> 课程列表
    </div>

    <el-card shadow="never">
      <div class="table-toolbar">
        <el-input v-model="keyword" placeholder="搜索课程名称/编号" clearable style="width: 300px;" @clear="loadCourses" @keyup.enter="loadCourses">
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="loadCourses">
          <el-icon><Search /></el-icon> 搜索
        </el-button>
      </div>

      <el-table :data="courses" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="courseCode" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column label="学分" width="60" prop="credit" />
        <el-table-column label="容量" width="100">
          <template #default="{ row }">
            {{ row.currentCount }}/{{ row.maxCapacity }}
            <el-tag v-if="row.currentCount >= row.maxCapacity" size="small" type="danger">满员</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="schedule" label="上课时间" width="180" />
        <el-table-column prop="classroom" label="地点" width="120" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="router.push(`/courses/${row.id}`)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-tag v-if="isSelected(row.id)" size="small" type="success" style="margin-left:4px;">已选</el-tag>
            <el-button
              v-else-if="userStore.isStudent && row.status === 1 && row.currentCount < row.maxCapacity"
              size="small"
              type="success"
              link
              @click="handleSelect(row.id)"
            >
              <el-icon><Plus /></el-icon> 选课
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 16px; text-align: right;">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @change="loadCourses"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCourses } from '@/api/course'
import { selectCourse, getMyCourses } from '@/api/selection'
import { useUserStore } from '@/stores/user'
import type { Course } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const courses = ref<Course[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const selectedCourseIds = ref<Set<number>>(new Set())

/** 判断某课程是否已被当前学生选中 */
function isSelected(courseId: number) {
  return selectedCourseIds.value.has(courseId)  // 在已选集合中查找
}

onMounted(async () => {
  await loadSelectedIds()
  await loadCourses()
})

/** 加载学生已选课程 ID 列表，用于标识已选课程 */
async function loadSelectedIds() {
  if (!userStore.isStudent) return
  try {
    const res = await getMyCourses()  // 获取我的课程列表
    const list: any[] = res.data || []
    selectedCourseIds.value = new Set(list.map((c: any) => c.id))  // 提取 ID 存入 Set
  } catch { /* handled */ }
}

/** 加载课程分页列表 */
async function loadCourses() {
  loading.value = true
  try {
    const res = await getCourses({ page: page.value, size: size.value, keyword: keyword.value || undefined })  // 分页查询
    courses.value = res.data.records  // 课程列表数据
    total.value = res.data.total  // 总记录数
  } finally {
    loading.value = false
  }
}

/** 选课操作 */
async function handleSelect(courseId: number) {
  try {
    await selectCourse(courseId)  // 调用选课接口
    ElMessage.success('选课成功')  // 成功提示
    loadCourses()  // 刷新列表
  } catch { /* handled by interceptor */ }
}
</script>
