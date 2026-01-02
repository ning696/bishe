import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

// Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// Pinia
import pinia from './store'

// Router
import router from './router'
import { setupRouterGuard } from './router/permission'

const app = createApp(App)

// 注册 Element Plus
app.use(ElementPlus)

// 注册 Pinia
app.use(pinia)

// 注册 Router
app.use(router)

// 注册路由守卫
setupRouterGuard(router)

app.mount('#app')
