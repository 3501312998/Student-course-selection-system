<!-- 管理员课程管理页（开关课程） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Document /></el-icon> 课程管理
    </div>

    <el-card shadow="never">
      <el-table :data="courses" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="courseCode" label="编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column label="教师ID" width="80" prop="teacherId" />
        <el-table-column label="容量" width="100">
          <template #default="{ row }">
            {{ row.currentCount }}/{{ row.maxCapacity }}
          </template>
        </el-table-column>
        <el-table-column prop="semester" label="学期" width="140" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              :type="row.status === 1 ? 'warning' : 'success'"
              link
              @click="handleToggle(row.id)"
            >
              {{ row.status === 1 ? '关闭' : '开放' }}
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
import { getCourses, toggleCourseStatus } from '@/api/course'
import type { Course } from '@/types'

const courses = ref<Course[]>([])
const loading = ref(false)

onMounted(() => loadCourses())

async function loadCourses() {
  loading.value = true
  try {
    const res = await getCourses({ page: 1, size: 999 })
    courses.value = res.data.records
  } finally {
    loading.value = false
  }
}

async function handleToggle(id: number) {
  try {
    await ElMessageBox.confirm('确定要切换课程状态吗？', '提示', { type: 'warning' })
    await toggleCourseStatus(id)
    ElMessage.success('操作成功')
    loadCourses()
  } catch { /* cancelled or error */ }
}
</script>
