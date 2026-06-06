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
            <el-button
              v-if="userStore.isStudent && row.status === 1 && row.currentCount < row.maxCapacity"
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
import { selectCourse } from '@/api/selection'
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

onMounted(() => loadCourses())

async function loadCourses() {
  loading.value = true
  try {
    const res = await getCourses({ page: page.value, size: size.value, keyword: keyword.value || undefined })
    courses.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

async function handleSelect(courseId: number) {
  try {
    await selectCourse(courseId)
    ElMessage.success('选课成功')
    loadCourses()
  } catch { /* handled by interceptor */ }
}
</script>
