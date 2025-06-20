<template>
  <div class="form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="goBack">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>{{ areaInfo ? areaInfo.areaName : '加载中...' }}</h1>
    </div>

    <div class="form-content" v-if="areaInfo">
      <!-- 区域信息 -->
      <div class="form-section">
        <div class="form-title">区域信息</div>
        <div class="info-item">
          <span class="info-label">区域编号:</span>
          <span class="info-value">{{ areaInfo.areaCode }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">区域类型:</span>
          <span class="info-value">{{ areaInfo.areaTypeName }}</span>
        </div>
      </div>

      <!-- 动态巡检项目 -->
      <div class="form-section" v-if="inspectionItems.length > 0">
        <div class="form-title">巡检项目</div>
        <div class="form-item" v-for="item in inspectionItems" :key="item.id">
          <label class="item-label" :for="'item-' + item.id">{{ item.itemName }}</label>
          <div class="item-control">
            <!-- 是/否 类型 -->
            <a-switch v-if="item.itemType === 'boolean'" v-model:checked="item.itemValue" checked-children="是" un-checked-children="否" />
            <!-- 数字类型 -->
            <a-input-number v-if="item.itemType === 'number'" v-model:value="item.itemValue" placeholder="请输入数值" />
            <!-- 文本类型 -->
            <a-input v-if="item.itemType === 'text'" v-model:value="item.itemValue" placeholder="请输入文本信息" />
          </div>
        </div>
      </div>
      <div class="form-section" v-else>
        <p>当前区域无巡检项</p>
      </div>

      <!-- 备注信息 -->
      <div class="form-section">
        <div class="form-title">备注信息</div>
        <a-textarea v-model:value="remark" placeholder="请输入备注信息" :rows="4" />
      </div>

      <!-- 提交按钮 -->
      <div class="form-actions">
        <a-button type="primary" block size="large" @click="submitForm" :loading="isSubmitting" :disabled="!areaInfo">
          提 交
        </a-button>
      </div>
    </div>
    <div class="loading-placeholder" v-else>
      <a-spin size="large" />
      <p>正在加载...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getAreaByCode, createRecord } from '@/api/inspection'
import { showToast } from 'vant';

const router = useRouter()
const route = useRoute()

const areaInfo = ref(null)
const inspectionItems = ref([])
const remark = ref('')
const isSubmitting = ref(false)

const goBack = () => router.back();

// 获取区域信息并直接处理巡检项
async function loadAreaInfo() {
  try {
    const areaCode = route.query.code
    if (!areaCode) {
      showToast('缺少区域编码');
      router.push('/scan');
      return;
    }
    const res = await getAreaByCode(areaCode);
    if (res.code === 200 || res.code === 0) {
      areaInfo.value = res.data;
      if (areaInfo.value && areaInfo.value.inspectionItems) {
        // 直接从返回结果中获取巡检项目
        inspectionItems.value = areaInfo.value.inspectionItems.map(item => {
            let defaultValue = null;
            if (item.itemType === 'boolean') {
                defaultValue = item.defaultValue === 'true';
            } else if (item.itemType === 'number') {
                defaultValue = item.defaultValue ? Number(item.defaultValue) : null;
            } else {
                defaultValue = item.defaultValue || '';
            }
            return { ...item, itemValue: defaultValue };
        });
      }
    } else {
      throw new Error(res.message || '获取区域信息失败');
    }
  } catch (err) {
    console.error('获取区域信息失败:', err);
    showToast(err.message || '获取区域信息失败');
    router.push('/scan');
  }
}

async function submitForm() {
  if (!areaInfo.value) {
    showToast('区域信息不存在，无法提交');
    return;
  }

  // 构造提交数据
  const recordDetails = inspectionItems.value.map(item => {
    if (item.isRequired && (item.itemValue === null || item.itemValue === '')) {
       showToast(`请填写必填项: ${item.itemName}`);
       throw new Error(`请填写必填项: ${item.itemName}`);
    }
    return {
      templateId: item.id,
      itemName: item.itemName,
      itemCode: item.itemCode,
      itemType: item.itemType,
      itemValue: String(item.itemValue),
      isNormal: item.itemType === 'boolean' ? item.itemValue : null,
    };
  });

  const payload = {
    areaId: areaInfo.value.id,
    inspectorId: 1, // TODO: 从用户信息获取
    inspectorName: '测试员', // TODO: 从用户信息获取
    inspectionDate: new Date().toISOString(),
    status: 'completed',
    totalItems: recordDetails.length,
    normalItems: recordDetails.filter(d => d.isNormal === true).length,
    abnormalItems: recordDetails.filter(d => d.isNormal === false).length,
    remark: remark.value,
    details: recordDetails
  }

  isSubmitting.value = true;
  try {
    const res = await createRecord(payload);
    if (res.code === 200 || res.code === 0) {
      showToast('提交成功');
      router.push('/records');
    } else {
      throw new Error(res.message || '提交失败');
    }
  } catch (err) {
    console.error('提交巡检记录失败:', err);
    showToast(err.message || '提交失败，请重试');
  } finally {
    isSubmitting.value = false;
  }
}

onMounted(() => {
  loadAreaInfo();
})
</script>

<style scoped>
.form-container {
    padding-bottom: 80px;
}
.form-header {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16px;
    background-color: var(--primary-color, #2196F3);
    color: white;
    position: sticky;
    top: 0;
    z-index: 10;
}
.form-header h1 {
    font-size: 1.2rem;
    margin: 0;
    color: white;
}
.back-button {
    position: absolute;
    left: 16px;
    color: white;
}
.form-content {
    padding: 16px;
}
.form-section {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    margin-bottom: 16px;
    padding: 16px;
}
.form-title {
    font-size: 1rem;
    font-weight: 600;
    margin-bottom: 12px;
}
.form-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
}
.form-item:last-child {
    border-bottom: none;
}
.item-label {
    flex: 1;
}
.item-control {
    flex-shrink: 0;
    width: 120px;
    text-align: right;
}
.info-item {
    display: flex;
    margin-bottom: 8px;
}
.info-label {
    color: #666;
    width: 80px;
}
.form-actions {
    padding: 16px;
}
.loading-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px;
}
</style> 