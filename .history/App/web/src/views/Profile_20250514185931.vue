<template>
  <div class="profile-container">
    <header class="profile-header">
      <h1>个人中心</h1>
    </header>
    <main class="profile-content">
      <div class="profile-info">
        <p>用户名: 张三</p>
        <p>部门: 技术部</p>
        <p>角色: 巡检员</p>
      </div>
      <button class="action-button">退出登录</button>
    </main>
  </div>
</template>

<script>
export default {
  name: 'Profile'
}
</script>

<style>
.profile-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.profile-header {
  margin-bottom: 32px;
}

.profile-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #000;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.profile-info {
  font-size: 16px;
  color: #333;
}

.action-button {
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 12px 24px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button:hover {
  background-color: #005bb5;
}
</style> 