<template>
  <div class="app-container">
    <header class="app-header">
      <h1>巡检App</h1>
    </header>
    <main class="app-content">
      <button class="action-button">扫码开始巡检</button>
      <button class="action-button">查看巡检记录</button>
      <button class="action-button">个人中心</button>
    </main>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
.app-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.app-header {
  margin-bottom: 32px;
}

.app-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #000;
}

.app-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
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