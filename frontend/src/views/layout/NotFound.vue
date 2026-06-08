<!-- 404 页面 -->
<template>
  <div class="not-found-page">
    <el-result icon="warning" title="404" sub-title="抱歉，页面不存在">
      <template #extra>
        <el-button type="primary" @click="router.push('/home')">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<style scoped>
.not-found-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}
</style>
