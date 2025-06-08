<template>
  <div class="scan-container">
    <header class="scan-header">
      <h1>扫码巡检</h1>
    </header>
    <main class="scan-content">
      <button class="action-button">开始扫码</button>
    </main>
  </div>
</template>

<script>
export default {
  name: 'Scan'
}
</script>

<style>
.scan-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.scan-header {
  margin-bottom: 32px;
}

.scan-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #000;
}

.scan-content {
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