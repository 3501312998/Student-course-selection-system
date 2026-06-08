// 应用入口
// 创建 Vue 应用实例，注册 Pinia、Router、Element Plus 等插件
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/styles/main.css'

const app = createApp(App)  // 创建 Vue 应用实例

// 注册所有 Element Plus 图标组件，使其可在模板中直接使用
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {  // 遍历所有图标
  app.component(key, component)
}

app.use(createPinia())  // 注册 Pinia 状态管理
app.use(router)  // 注册 Vue Router
app.use(ElementPlus)  // 注册 Element Plus UI 库
app.mount('#app')  // 挂载应用到 #app 容器
