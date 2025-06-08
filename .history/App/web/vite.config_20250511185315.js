import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

export default defineConfig({
  plugins: [vue()],
  base: './',
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  build: {
    outDir: '../android/app/src/main/assets', // 打包到Android assets目录
    emptyOutDir: true
  },
  server: {
    port: 5173,
    open: true,
    host: true
  }
}); 