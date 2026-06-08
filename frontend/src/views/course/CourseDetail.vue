<!-- 课程详情页 -->
<template>
  <div class="page-container">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <span><el-icon><Document /></el-icon> 课程详情</span>
        <el-button style="float: right;" @click="router.back()">返回</el-button>
      </template>

      <el-descriptions :column="2" border v-if="course">
        <el-descriptions-item label="课程名称" :span="2">{{ course.courseName }}</el-descriptions-item>
        <el-descriptions-item label="课程编号">{{ course.courseCode }}</el-descriptions-item>
        <el-descriptions-item label="学分">{{ course.credit }}</el-descriptions-item>
        <el-descriptions-item label="容量">{{ course.currentCount }}/{{ course.maxCapacity }}</el-descriptions-item>
        <el-descriptions-item label="学期">{{ course.semester }}</el-descriptions-item>
        <el-descriptions-item label="上课时间">{{ course.schedule }}</el-descriptions-item>
        <el-descriptions-item label="上课地点">{{ course.classroom }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="course.status === 1 ? 'success' : 'info'">
            {{ course.status === 1 ? '开放选课' : '已关闭' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="课程描述" :span="2">{{ course.description || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 20px; text-align: center;" v-if="course">
        <el-tag v-if="userStore.isStudent && isSelected" size="large" type="success" style="margin-bottom:8px;">
          <el-icon><CircleCheck /></el-icon> 已选该课程
        </el-tag>
        <el-button
          v-if="userStore.isStudent && !isSelected && course.status === 1 && course.currentCount < course.maxCapacity"
          type="primary"
          size="large"
          @click="handleSelect"
        >
          <el-icon><Plus /></el-icon> 选课
        </el-button>
        <el-button
          v-if="userStore.isStudent && isSelected"
          size="large"
          @click="handleDrop"
        >
          <el-icon><Delete /></el-icon> 退课
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCourseDetail } from '@/api/course'
import { selectCourse, dropCourse, getMyCourses } from '@/api/selection'
import { useUserStore } from '@/stores/user'
import type { Course } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const course = ref<Course | null>(null)
const loading = ref(false)
const isSelected = ref(false)  // 当前课程是否已被该学生选中

onMounted(async () => {  // 组件挂载后加载课程详情
  loading.value = true
  try {
    const res = await getCourseDetail(Number(route.params.id))  // 根据路由参数获取课程详情
    course.value = res.data  // 保存课程数据
  } finally {
    loading.value = false
  }
  if (userStore.isStudent) {
    try {  // 学生额外检查是否已选该课程
      const myRes = await getMyCourses()  // 获取已选课程
      const list: any[] = myRes.data || []
      isSelected.value = list.some((c: any) => c.id === Number(route.params.id))  // 判断当前课程是否在已选列表中
    } catch { /* handled */ }
  }
})

/** 选课 */
async function handleSelect() {
  try {
    await selectCourse(course.value!.id)  // 调用选课接口
    ElMessage.success('选课成功')
  } catch { /* handled */ }
}

/** 退课 */
async function handleDrop() {
  try {
    await dropCourse(course.value!.id)  // 调用退课接口
    ElMessage.success('退课成功')
  } catch { /* handled */ }
}
</script>
