<!-- 成绩管理页（教师查看/录入/修改所授课程成绩） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Edit /></el-icon> 成绩管理
    </div>

    <!-- 课程选择 -->
    <el-card shadow="never" style="margin-bottom: 20px;">
      <div style="display: flex; align-items: center; gap: 16px; flex-wrap: wrap;">
        <span style="font-weight: 600;">选择课程：</span>
        <el-select
          v-model="selectedCourseId"
          placeholder="请选择课程"
          style="width: 360px;"
          @change="onCourseChange"
          clearable
          filterable
        >
          <el-option
            v-for="c in courses"
            :key="c.id"
            :label="`${c.courseName} (${c.courseCode})`"
            :value="c.id"
          />
        </el-select>
        <el-tag v-if="selectedCourse" type="info">
          {{ selectedCourse.semester }} | {{ selectedCourse.currentCount }}/{{ selectedCourse.maxCapacity }}人
        </el-tag>
      </div>
    </el-card>

    <!-- 学生列表 -->
    <el-card shadow="never" v-if="selectedCourseId">
      <template #header>
        <span>
          <el-icon><UserFilled /></el-icon>
          {{ selectedCourse?.courseName }} - 学生名单
          <el-tag style="margin-left: 8px;" :type="stats.avg ? 'success' : 'info'" size="small">
            {{ stats.total }}人 | 已录{{ stats.scored }}人 | 均分{{ stats.avg || '-' }}
          </el-tag>
        </span>
      </template>

      <el-table :data="students" v-loading="loading" stripe style="width: 100%">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column label="成绩" width="160">
          <template #default="{ row }">
            <el-input-number
              v-model="row.scoreTemp"
              :min="0"
              :max="100"
              :precision="1"
              :step="1"
              size="small"
              :style="{ width: '140px' }"
              placeholder="输入成绩"
            />
          </template>
        </el-table-column>
        <el-table-column label="当前成绩" width="100">
          <template #default="{ row }">
            <el-tag
              v-if="row.score !== null && row.score !== undefined"
              :type="row.score >= 60 ? 'success' : 'danger'"
              size="small"
            >
              {{ row.score }}
            </el-tag>
            <span v-else style="color: #909399;">未录入</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 3" size="small" type="success">已结课</el-tag>
            <el-tag v-else-if="row.status === 2" size="small" type="info">已退课</el-tag>
            <el-tag v-else size="small" type="warning">在学习</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              type="primary"
              @click="handleSave(row)"
              :disabled="row.scoreTemp === undefined || row.scoreTemp === null || row.scoreTemp === ''"
            >
              <el-icon><Check /></el-icon> 保存
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && students.length === 0" class="empty-state">
        <el-empty description="该课程暂无学生选课" />
      </div>

      <!-- 批量操作 -->
      <div v-if="students.length > 0" style="margin-top: 16px; display: flex; gap: 12px;">
        <el-button type="primary" @click="handleBatchSave" :loading="batchSaving">
          <el-icon><CircleCheck /></el-icon> 批量保存所有成绩
        </el-button>
        <el-button @click="resetAllScores">
          <el-icon><RefreshCw /></el-icon> 重置输入
        </el-button>
      </div>
    </el-card>

    <!-- 未选课时 -->
    <el-card shadow="never" v-if="!selectedCourseId">
      <el-empty description="请先选择一门课程" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getTeacherCourses } from '@/api/selection'
import { getCourseStudents, setScore } from '@/api/selection'
import type { Course } from '@/types'

const courses = ref<Course[]>([])
const selectedCourseId = ref<number | null>(null)
const selectedCourse = computed(() =>
  courses.value.find((c) => c.id === selectedCourseId.value)
)
const students = ref<any[]>([])
const loading = ref(false)
const batchSaving = ref(false)

// 统计
const stats = computed(() => {
  const total = students.value.length
  const scored = students.value.filter((s) => s.score !== null && s.score !== undefined).length
  const scores = students.value
    .filter((s) => s.score !== null && s.score !== undefined)
    .map((s) => Number(s.score))
  const avg = scores.length > 0 ? (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1) : null
  return { total, scored, avg }
})

onMounted(async () => {
  try {
    const res = await getTeacherCourses()
    courses.value = res.data || []
  } catch { /* handled */ }
})

async function onCourseChange() {
  if (!selectedCourseId.value) {
    students.value = []
    return
  }
  await loadStudents()
}

async function loadStudents() {
  loading.value = true
  try {
    const res = await getCourseStudents(selectedCourseId.value!)
    students.value = (res.data || []).map((s: any) => ({
      ...s,
      scoreTemp: s.score, // 预填当前成绩
    }))
  } finally {
    loading.value = false
  }
}

async function handleSave(row: any) {
  if (row.scoreTemp === undefined || row.scoreTemp === null) {
    ElMessage.warning('请输入成绩')
    return
  }
  try {
    await setScore({
      studentId: row.studentId,
      courseId: selectedCourseId.value!,
      score: row.scoreTemp,
    })
    row.score = row.scoreTemp
    ElMessage.success(`${row.studentName} 成绩保存成功`)
  } catch { /* handled */ }
}

async function handleBatchSave() {
  const toSave = students.value.filter(
    (s) => s.scoreTemp !== undefined && s.scoreTemp !== null && s.scoreTemp !== ''
  )
  if (toSave.length === 0) {
    ElMessage.warning('没有需要保存的成绩')
    return
  }
  batchSaving.value = true
  let success = 0
  for (const row of toSave) {
    try {
      await setScore({
        studentId: row.studentId,
        courseId: selectedCourseId.value!,
        score: row.scoreTemp,
      })
      row.score = row.scoreTemp
      success++
    } catch {
      // 跳过单个失败
    }
  }
  batchSaving.value = false
  ElMessage.success(`成功保存 ${success}/${toSave.length} 条成绩`)
}

function resetAllScores() {
  students.value.forEach((s) => {
    s.scoreTemp = s.score
  })
  ElMessage.info('已重置')
}
</script>
