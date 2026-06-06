<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Edit /></el-icon> 我的课程
    </div>

    <el-card shadow="never">
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="showCreate = true">
          <el-icon><Plus /></el-icon> 新建课程
        </el-button>
      </div>

      <el-table :data="courses" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="courseCode" label="编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column prop="credit" label="学分" width="60" />
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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="router.push(`/teacher-courses/${row.id}/students`)">
              <el-icon><View /></el-icon> 学生名单
            </el-button>
            <el-button size="small" type="warning" link @click="editCourse(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建/编辑课程对话框 -->
    <el-dialog v-model="showCreate" :title="isEditing ? '编辑课程' : '新建课程'" width="600px">
      <el-form :model="courseForm" label-width="100px">
        <el-form-item label="课程名称" required>
          <el-input v-model="courseForm.courseName" />
        </el-form-item>
        <el-form-item label="课程编号" required>
          <el-input v-model="courseForm.courseCode" :disabled="isEditing" />
        </el-form-item>
        <el-form-item label="学分" required>
          <el-input-number v-model="courseForm.credit" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="最大容量" required>
          <el-input-number v-model="courseForm.maxCapacity" :min="10" :max="200" />
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="courseForm.semester" placeholder="如 2025-2026-1" />
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="courseForm.schedule" placeholder="如 周一 1-2节" />
        </el-form-item>
        <el-form-item label="上课地点">
          <el-input v-model="courseForm.classroom" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="courseForm.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreate = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTeacherCourses } from '@/api/selection'
import * as courseApi from '@/api/course'
import type { Course } from '@/types'

const router = useRouter()
const courses = ref<Course[]>([])
const loading = ref(false)
const showCreate = ref(false)
const saving = ref(false)
const isEditing = ref(false)
const editId = ref<number | null>(null)

const courseForm = reactive({
  courseName: '',
  courseCode: '',
  credit: 3,
  maxCapacity: 60,
  semester: '2025-2026-1',
  schedule: '',
  classroom: '',
  description: '',
})

onMounted(() => loadCourses())

async function loadCourses() {
  loading.value = true
  try {
    const res = await getTeacherCourses()
    courses.value = res.data
  } finally {
    loading.value = false
  }
}

function editCourse(course: Course) {
  isEditing.value = true
  editId.value = course.id
  courseForm.courseName = course.courseName
  courseForm.courseCode = course.courseCode
  courseForm.credit = course.credit
  courseForm.maxCapacity = course.maxCapacity
  courseForm.semester = course.semester
  courseForm.schedule = course.schedule
  courseForm.classroom = course.classroom
  courseForm.description = course.description || ''
  showCreate.value = true
}

function resetForm() {
  isEditing.value = false
  editId.value = null
  courseForm.courseName = ''
  courseForm.courseCode = ''
  courseForm.credit = 3
  courseForm.maxCapacity = 60
  courseForm.semester = '2025-2026-1'
  courseForm.schedule = ''
  courseForm.classroom = ''
  courseForm.description = ''
}

async function handleSave() {
  if (!courseForm.courseName || !courseForm.courseCode) {
    ElMessage.warning('请填写课程名称和编号')
    return
  }
  saving.value = true
  try {
    if (isEditing.value && editId.value) {
      // Use courseApi for update
      await courseApi.updateCourse(editId.value, courseForm as any)
      ElMessage.success('更新成功')
    } else {
      await courseApi.createCourse(courseForm as any)
      ElMessage.success('创建成功')
    }
    showCreate.value = false
    resetForm()
    loadCourses()
  } finally {
    saving.value = false
  }
}
</script>
