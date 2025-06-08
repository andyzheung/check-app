<template>
  <div class="profile-page">
    <header class="header">
      <button class="back-btn" @click="router.back()">返回</button>
      <h1>个人信息</h1>
    </header>
    
    <div class="profile-content">
      <div class="avatar-section">
        <div class="avatar">
          <img src="https://via.placeholder.com/100" alt="头像" />
        </div>
        <h2>{{ userInfo.name }}</h2>
        <p class="role">{{ userInfo.role }}</p>
      </div>
      
      <div class="info-section">
        <div class="info-item">
          <label>工号</label>
          <span>{{ userInfo.employeeId }}</span>
        </div>
        <div class="info-item">
          <label>部门</label>
          <span>{{ userInfo.department }}</span>
        </div>
        <div class="info-item">
          <label>手机</label>
          <span>{{ userInfo.phone }}</span>
        </div>
        <div class="info-item">
          <label>邮箱</label>
          <span>{{ userInfo.email }}</span>
        </div>
      </div>
      
      <div class="action-buttons">
        <button class="action-btn" @click="handleEdit">
          <i class="icon-edit"></i>
          编辑信息
        </button>
        <button class="action-btn" @click="handleChangePassword">
          <i class="icon-password"></i>
          修改密码
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userInfo = ref({
  name: '张三',
  role: '巡检员',
  employeeId: 'EMP001',
  department: '设备部',
  phone: '13800138000',
  email: 'zhangsan@example.com'
})

const handleEdit = () => {
  // TODO: 实现编辑信息功能
  console.log('编辑信息')
}

const handleChangePassword = () => {
  // TODO: 实现修改密码功能
  console.log('修改密码')
}
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');

.profile-page {
  min-height: 100vh;
  background: #f7f7f7;
}

.header {
  background: #4CAF50;
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  position: relative;
}

.back-btn {
  background: transparent;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
  position: absolute;
  left: 20px;
}

h1 {
  width: 100%;
  text-align: center;
  margin: 0;
  font-size: 18px;
}

.profile-content {
  padding: 20px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 30px;
}

.avatar {
  width: 100px;
  height: 100px;
  margin: 0 auto 15px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #4CAF50;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

h2 {
  margin: 0 0 5px;
  color: #333;
}

.role {
  color: #666;
  margin: 0;
}

.info-section {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item label {
  color: #666;
}

.info-item span {
  color: #333;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.action-btn {
  padding: 12px;
  background: white;
  border: 1px solid #4CAF50;
  color: #4CAF50;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
}

.action-btn:hover {
  background: #4CAF50;
  color: white;
}

.icon-edit, .icon-password {
  font-size: 20px;
}
</style> 