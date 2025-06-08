<template>
  <div class="inspection-form-container">
    <header class="inspection-form-header">
      <h1>巡检表单</h1>
    </header>
    <main class="inspection-form-content">
      <div class="form-group">
        <label>巡检区域</label>
        <input type="text" placeholder="请输入巡检区域" class="input-field" />
      </div>
      <div class="form-group">
        <label>巡检项目</label>
        <input type="text" placeholder="请输入巡检项目" class="input-field" />
      </div>
      <button class="action-button">提交</button>
    </main>
  </div>
</template>

<script>
export default {
  name: 'InspectionForm'
}
</script>

<style>
.inspection-form-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.inspection-form-header {
  margin-bottom: 32px;
}

.inspection-form-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #000;
}

.inspection-form-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-field {
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
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