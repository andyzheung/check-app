import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  base: './',
  build: {
    outDir: '../android/app/src/main/assets', // 打包到Android assets目录
    emptyOutDir: true
  }
}); 