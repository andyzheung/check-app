<template>
  <div class="personal-info-container">
    <!-- 顶部导航 -->
    <div class="personal-info-header">
      <a class="back-button" @click.prevent="router.push('/profile')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>个人信息</h1>
    </div>

    <!-- 个人信息表单 -->
    <div class="personal-info-content">
      <div class="info-form">
        <div class="form-item">
          <label>工号</label>
          <div class="form-value">{{ userInfo.username }}</div>
        </div>
        <div class="form-item">
          <label>姓名</label>
          <div class="form-value">{{ userInfo.realName }}</div>
        </div>
        <div class="form-item">
          <label>部门</label>
          <div class="form-value">{{ userInfo.department }}</div>
        </div>
        <div class="form-item">
          <label>角色</label>
          <div class="form-value">{{ formatRole(userInfo.role) }}</div>
        </div>
        <div class="form-item">
          <label>状态</label>
          <div class="form-value" :class="userInfo.status">
            {{ formatStatus(userInfo.status) }}
          </div>
        </div>
      </div>

      <!-- 修改密码按钮 -->
      <div class="change-password-btn" @click="showChangePasswordDialog">
        修改密码
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <van-dialog
      v-model:show="showDialog"
      title="修改密码"
      show-cancel-button
      @confirm="handleChangePassword"
      :before-close="handleDialogClose"
    >
      <div class="password-form">
        <van-field
          v-model="passwordForm.oldPassword"
          type="password"
          label="原密码"
          placeholder="请输入原密码"
          :rules="[{ required: true, message: '请输入原密码' }]"
        />
        <van-field
          v-model="passwordForm.newPassword"
          type="password"
          label="新密码"
          placeholder="请输入新密码"
          :rules="[{ required: true, message: '请输入新密码' }]"
        />
        <van-field
          v-model="passwordForm.confirmPassword"
          type="password"
          label="确认密码"
          placeholder="请再次输入新密码"
          :rules="[{ required: true, message: '请再次输入新密码' }]"
        />
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '@/utils/request'

const router = useRouter()
const userInfo = ref({})
const showDialog = ref(false)
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 格式化角色
function formatRole(role) {
  const roleMap = {
    admin: '管理员',
    user: '普通用户'
  }
  return roleMap[role] || role
}

// 格式化状态
function formatStatus(status) {
  const statusMap = {
    active: '正常',
    inactive: '禁用'
  }
  return statusMap[status] || status
}

// 获取用户信息
async function fetchUserInfo() {
  try {
    const res = await request.get('/users/current')
    if (res.code === 200 || res.code === 0) {
      userInfo.value = res.data
    }
  } catch (err) {
    console.error('获取用户信息失败:', err)
  }
}

// 显示修改密码弹窗
function showChangePasswordDialog() {
  showDialog.value = true
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

// 处理弹窗关闭
function handleDialogClose(action) {
  if (action === 'confirm') {
    // 验证表单
    if (!passwordForm.value.oldPassword) {
      showToast('请输入原密码')
      return false
    }
    if (!passwordForm.value.newPassword) {
      showToast('请输入新密码')
      return false
    }
    if (!passwordForm.value.confirmPassword) {
      showToast('请再次输入新密码')
      return false
    }
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      showToast('两次输入的密码不一致')
      return false
    }
  }
  return true
}

// 处理修改密码
async function handleChangePassword() {
  try {
    const res = await request.put('/users/password', {
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    if (res.code === 200 || res.code === 0) {
      showToast('密码修改成功')
      // 退出登录
      await request.post('/auth/logout')
      localStorage.removeItem('token')
      router.push('/login')
    } else {
      showToast(res.message || '密码修改失败')
    }
  } catch (err) {
    console.error('修改密码失败:', err)
    showToast('修改密码失败')
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.personal-info-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
}

.personal-info-header {
  padding: 28px 0 18px 0;
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-color, #2196F3);
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.personal-info-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.personal-info-content {
  padding: 20px;
}

.info-form {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.form-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.form-item:last-child {
  border-bottom: none;
}

.form-item label {
  width: 80px;
  color: #666;
  font-size: 15px;
}

.form-value {
  flex: 1;
  color: #333;
  font-size: 15px;
}

.form-value.active {
  color: #4CAF50;
}

.form-value.inactive {
  color: #F44336;
}

.change-password-btn {
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  text-align: center;
  color: var(--primary-color, #2196F3);
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.change-password-btn:active {
  background: #f5f5f5;
}

.password-form {
  padding: 20px;
}

:deep(.van-field) {
  padding: 10px 0;
}

:deep(.van-dialog__content) {
  padding-top: 20px;
}
</style> 