<!-- 成绩查询页（学生查看成绩） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><View /></el-icon> 成绩查询
    </div>

    <el-card shadow="never">
      <el-table :data="grades" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column prop="courseCode" label="课程编号" width="120" />
        <el-table-column prop="credit" label="学分" width="60" />
        <el-table-column label="成绩" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.score !== null && row.score !== undefined" :type="row.score >= 60 ? 'success' : 'danger'" size="large">
              {{ row.score }}
            </el-tag>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.score !== null && row.score !== undefined" type="success">已结课</el-tag>
            <el-tag v-else-if="row.status === 2" type="info">已退课</el-tag>
            <el-tag v-else type="warning">在学习</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="selectTime" label="选课时间" width="180" />
      </el-table>

      <div v-if="!loading && grades.length === 0" class="empty-state">
        <el-empty description="暂无成绩记录" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getGrades } from '@/api/selection'

const grades = ref<any[]>([])
const loading = ref(false)

onMounted(() => loadGrades())

async function loadGrades() {
  loading.value = true
  try {
    const res = await getGrades()
    grades.value = res.data
  } finally {
    loading.value = false
  }
}
</script>
