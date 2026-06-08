<!-- 成绩录入页（教师录入学生成绩） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Edit /></el-icon> 学生名单 - {{ courseName }}
      <el-button style="margin-left: 16px;" @click="router.back()">返回</el-button>
    </div>

    <el-card shadow="never">
      <el-table :data="students" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column label="成绩" width="120">
          <template #default="{ row }">
            <el-input-number
              v-model="row.scoreTemp"
              :min="0"
              :max="100"
              :precision="1"
              :step="1"
              size="small"
              :disabled="row.score !== null"
              style="width: 130px;"
            />
          </template>
        </el-table-column>
        <el-table-column label="当前成绩" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.score !== null" :type="row.score >= 60 ? 'success' : 'danger'">
              {{ row.score }}
            </el-tag>
            <span v-else style="color: #909399;">未录入</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
              size="small"
              type="primary"
              @click="handleSave(row)"
              :disabled="row.scoreTemp === undefined || row.scoreTemp === null"
            >
              <el-icon><Check /></el-icon> 录入
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && students.length === 0" class="empty-state">
        <el-empty description="暂无学生选课" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCourseStudents, setScore } from '@/api/selection'
import { getCourseDetail } from '@/api/course'

const route = useRoute()
const router = useRouter()

const courseId = Number(route.params.id)  // 从路由参数获取课程 ID
const courseName = ref('')
const students = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {  // 组件挂载后加载课程信息和学生名单
  await loadCourseInfo()
  await loadStudents()
})

/** 获取课程名称 */
async function loadCourseInfo() {
  try {
    const res = await getCourseDetail(courseId)  // 调用课程详情接口
    courseName.value = res.data.courseName  // 保存课程名称
  } catch { /* handled */ }
}

/** 获取选课学生名单 */
async function loadStudents() {
  loading.value = true
  try {
    const res = await getCourseStudents(courseId)  // 调用学生名单接口
    students.value = (res.data || []).map((s: any) => ({
      // 初始化临时成绩字段，已存在的预填到输入框
      ...s,
      scoreTemp: undefined,
    }))
  } finally {
    loading.value = false
  }
}

/** 保存单个学生成绩 */
async function handleSave(row: any) {
  try {
    await setScore({  // 调用成绩录入接口
      studentId: row.studentId,
      courseId,
      score: row.scoreTemp,
    })
    ElMessage.success('成绩录入成功')
    loadStudents()
  } catch { /* handled */ }
}
</script>
