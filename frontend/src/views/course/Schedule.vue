<!-- 我的课表页（学生查看已选课程） -->
<template>
  <div class="page-container">
    <div class="page-title">
      <el-icon><Clock /></el-icon> 我的课表
    </div>

    <el-card shadow="never">
      <el-table :data="schedule" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column prop="courseCode" label="课程编号" width="120" />
        <el-table-column prop="schedule" label="上课时间" width="180" />
        <el-table-column prop="classroom" label="上课地点" width="120" />
        <el-table-column prop="credit" label="学分" width="60" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" type="danger" link @click="handleDrop(row.id)">
              <el-icon><Delete /></el-icon> 退课
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && schedule.length === 0" class="empty-state">
        <el-empty description="暂无课程，请先去选课" />
        <el-button type="primary" @click="router.push('/courses')">去选课</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSchedule, dropCourse } from '@/api/selection'

const router = useRouter()
const schedule = ref<any[]>([])
const loading = ref(false)

onMounted(() => loadSchedule())  // 组件挂载后加载课表

/** 获取学生课表数据 */
async function loadSchedule() {
  loading.value = true
  try {
    const res = await getSchedule()  // 调用课表接口
    schedule.value = res.data  // 保存课表数据
  } finally {
    loading.value = false
  }
}

/** 退课：确认弹窗 -> 调用退课接口 -> 刷新课表 */
async function handleDrop(courseId: number) {
  try {
    await ElMessageBox.confirm('确定要退课吗？', '提示', { type: 'warning' })  // 确认弹窗
    await dropCourse(courseId)  // 调用退课接口
    ElMessage.success('退课成功')
    loadSchedule()
  } catch { /* cancelled or error */ }
}
</script>
