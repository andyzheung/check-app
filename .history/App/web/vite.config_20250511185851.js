import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  base: './',
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  build: {
    outDir: '../android/app/src/main/assets',
    emptyOutDir: true
  },
  server: {
    port: 5173,
    open: true,
    host: true
  }
}) 