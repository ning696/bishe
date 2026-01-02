import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    host: 'localhost', // 明确指定 host
    hmr: {
      // 配置 HMR WebSocket 连接
      protocol: 'ws',
      host: 'localhost',
      port: 5173,
      // 如果 WebSocket 连接失败，可以尝试禁用客户端错误提示
      clientPort: 5173
    },
    proxy: {
      '/dev-api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev-api/, '')
      },
      '/ws': {
        target: 'ws://localhost:8080',
        changeOrigin: true,
        ws: true,
        // 确保 WebSocket 请求路径保持不变
        rewrite: (path) => path
      }
    }
  }
})
