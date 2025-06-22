<template>
  <div class="form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="goBack">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>
        <span class="material-icons">{{ getAreaIcon() }}</span>
        {{ getAreaTitle() }}
      </h1>
    </div>

    <div class="form-content" v-if="areaInfo">
      <!-- 数据中心表单 -->
      <div v-if="areaInfo.areaType === 'D'">
        <!-- 安全检查 -->
        <div class="form-section">
          <div class="form-title">安全检查</div>
          <div class="form-item">
            <span>机房范围内是否有声光报警</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="sound_light_alarm" :value="true" v-model="soundLightAlarm">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="sound_light_alarm" :value="false" v-model="soundLightAlarm">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>机房内是否有纸箱、泡沫等火载量</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="fire_load_materials" :value="true" v-model="fireLoadMaterials">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="fire_load_materials" :value="false" v-model="fireLoadMaterials">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>机房内是否有漏水</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="room_leakage" :value="true" v-model="roomLeakage">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="room_leakage" :value="false" v-model="roomLeakage">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>天花是否有漏水</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="ceiling_leakage" :value="true" v-model="ceilingLeakage">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="ceiling_leakage" :value="false" v-model="ceilingLeakage">
                <span>否</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 环境检查 -->
        <div class="form-section">
          <div class="form-title">环境检查</div>
          <div class="form-item">
            <span>机房环境温度 (°C)</span>
            <div class="input-group">
              <input type="number" v-model="roomTemperature" placeholder="请输入温度值" step="0.1">
            </div>
          </div>
        </div>

        <!-- 模块检查 -->
        <div class="form-section">
          <div class="form-title">模块检查</div>
          <div v-if="areaInfo.modules && areaInfo.modules.length > 0" class="module-info">
            <span class="module-count-info">当前数据中心共有 {{ areaInfo.modules.length }} 个模块</span>
          </div>
          
          <div class="module-group" v-for="(module, index) in areaInfo.modules" :key="module.id">
            <div class="module-header">{{ module.name || `模块${index + 1}` }}</div>
            <div class="form-item">
              <span>回风温度 (°C)</span>
              <div class="input-group">
                <input type="number" v-model="moduleData[index].returnTemp" placeholder="请输入温度值" step="0.1">
              </div>
            </div>
            <div class="form-item">
              <span>供电是否正常</span>
              <div class="radio-group">
                <label class="radio-option">
                  <input type="radio" :name="'module' + module.id + '_power'" :value="true" v-model="moduleData[index].powerStatus">
                  <span>是</span>
                </label>
                <label class="radio-option">
                  <input type="radio" :name="'module' + module.id + '_power'" :value="false" v-model="moduleData[index].powerStatus">
                  <span>否</span>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 弱电间表单 -->
      <div v-else-if="areaInfo.areaType === 'E'">
        <!-- 漏水检查 -->
        <div class="form-section">
          <div class="form-title">漏水检查</div>
          <div class="form-item">
            <span>机房地面是否漏水</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="floor_leakage" :value="true" v-model="floorLeakage">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="floor_leakage" :value="false" v-model="floorLeakage">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>墙壁是否漏水</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="wall_leakage" :value="true" v-model="wallLeakage">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="wall_leakage" :value="false" v-model="wallLeakage">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>天花是否漏水</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="ceiling_leakage_e" :value="true" v-model="ceilingLeakageE">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="ceiling_leakage_e" :value="false" v-model="ceilingLeakageE">
                <span>否</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 安全检查 -->
        <div class="form-section">
          <div class="form-title">安全检查</div>
          <div class="form-item">
            <span>机房是否有垃圾纸箱等可燃物</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="combustible_materials" :value="true" v-model="combustibleMaterials">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="combustible_materials" :value="false" v-model="combustibleMaterials">
                <span>否</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 环境检查 -->
        <div class="form-section">
          <div class="form-title">环境检查</div>
          <div class="form-item">
            <span>机房环境温度 (°C)</span>
            <div class="input-group">
              <input type="number" v-model="roomTemperatureE" placeholder="请输入温度值" step="0.1">
            </div>
          </div>
        </div>

        <!-- 供电检查 -->
        <div class="form-section">
          <div class="form-title">供电检查</div>
          <div class="form-item">
            <span>市电供电是否正常</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="mains_power" :value="true" v-model="mainsPower">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="mains_power" :value="false" v-model="mainsPower">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>UPS供电是否正常</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="ups_power" :value="true" v-model="upsPower">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="ups_power" :value="false" v-model="upsPower">
                <span>否</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 设备检查 -->
        <div class="form-section">
          <div class="form-title">设备检查</div>
          <div class="form-item">
            <span>网络机柜是否正常上锁</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="cabinet_lock" :value="true" v-model="cabinetLock">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="cabinet_lock" :value="false" v-model="cabinetLock">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>机房门禁是否正常</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="access_control" :value="true" v-model="accessControl">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="access_control" :value="false" v-model="accessControl">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>机房设备运行是否有异响</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="equipment_noise" :value="true" v-model="equipmentNoise">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="equipment_noise" :value="false" v-model="equipmentNoise">
                <span>否</span>
              </label>
            </div>
          </div>
          <div class="form-item">
            <span>机房照明是否正常</span>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" name="lighting_check" :value="true" v-model="lightingCheck">
                <span>是</span>
              </label>
              <label class="radio-option">
                <input type="radio" name="lighting_check" :value="false" v-model="lightingCheck">
                <span>否</span>
              </label>
            </div>
          </div>
        </div>
      </div>

      <!-- 备注信息 -->
      <div class="form-section">
        <div class="form-title">备注信息</div>
        <div class="form-item">
          <textarea class="remarks-input" v-model="remark" placeholder="请输入备注信息（可选）"></textarea>
        </div>
      </div>

      <!-- 提交按钮 -->
      <div class="form-actions">
        <button class="submit-button" @click="submitForm" :disabled="isSubmitting">
          {{ isSubmitting ? '提交中...' : '提交' }}
        </button>
      </div>
    </div>
    <div class="loading-placeholder" v-else>
      <div class="loading-spinner"></div>
      <p>正在加载...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getAreaByCode, createRecord } from '@/api/inspection'

const router = useRouter()
const route = useRoute()

const areaInfo = ref(null)
const inspectionItems = ref([])
const remark = ref('')
const isSubmitting = ref(false)
const moduleData = ref([])

// 数据中心检查项目变量
const soundLightAlarm = ref(null)
const fireLoadMaterials = ref(null)
const roomLeakage = ref(null)
const ceilingLeakage = ref(null)
const roomTemperature = ref(null)

// 弱电间检查项目变量
const floorLeakage = ref(null)
const wallLeakage = ref(null)
const ceilingLeakageE = ref(null)
const combustibleMaterials = ref(null)
const roomTemperatureE = ref(null)
const mainsPower = ref(null)
const upsPower = ref(null)
const cabinetLock = ref(null)
const accessControl = ref(null)
const equipmentNoise = ref(null)
const lightingCheck = ref(null)

const goBack = () => router.back();

const getAreaIcon = () => {
  if (!areaInfo.value) return 'location_on'
  return areaInfo.value.areaType === 'D' ? 'dns' : 'router'
}

const getAreaTitle = () => {
  if (!areaInfo.value) return '加载中...'
  const typeText = areaInfo.value.areaType === 'D' ? '数据中心巡检' : '弱电间巡检'
  return `${typeText} - ${areaInfo.value.areaName}`
}

const initializeModuleData = () => {
  if (areaInfo.value && areaInfo.value.modules) {
    const moduleCount = areaInfo.value.modules.length
    moduleData.value = Array.from({ length: moduleCount }, () => ({
      returnTemp: null,
      powerStatus: null
    }))
  }
}

// 获取区域信息并直接处理巡检项
async function loadAreaInfo() {
  try {
    const areaCode = route.query.code
    if (!areaCode) {
      alert('缺少区域编码');
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
                defaultValue = item.defaultValue === 'true' ? true : false;
            } else if (item.itemType === 'number') {
                defaultValue = item.defaultValue ? Number(item.defaultValue) : null;
            } else {
                defaultValue = item.defaultValue || '';
            }
            return { ...item, itemValue: defaultValue };
        });
        
        // 如果是数据中心，初始化模块数据
        if (areaInfo.value.areaType === 'D') {
          initializeModuleData();
        }
      }
    } else {
      throw new Error(res.message || '获取区域信息失败');
    }
  } catch (err) {
    console.error('获取区域信息失败:', err);
    alert(err.message || '获取区域信息失败');
    router.push('/scan');
  }
}

async function submitForm() {
  if (!areaInfo.value) {
    alert('区域信息不存在，无法提交');
    return;
  }

  // 基本数据验证
  let hasValidData = false;
  
  // 检查是否有任何有效的检查项目数据
  if (areaInfo.value.areaType === 'D') {
    hasValidData = soundLightAlarm.value !== null || 
                   fireLoadMaterials.value !== null || 
                   roomLeakage.value !== null || 
                   ceilingLeakage.value !== null || 
                   roomTemperature.value !== null ||
                   moduleData.value.some(m => m.returnTemp !== null || m.powerStatus !== null);
  } else if (areaInfo.value.areaType === 'E') {
    hasValidData = floorLeakage.value !== null ||
                   wallLeakage.value !== null ||
                   ceilingLeakageE.value !== null ||
                   combustibleMaterials.value !== null ||
                   roomTemperatureE.value !== null ||
                   mainsPower.value !== null ||
                   upsPower.value !== null ||
                   cabinetLock.value !== null ||
                   accessControl.value !== null ||
                   equipmentNoise.value !== null ||
                   lightingCheck.value !== null;
  }
  
  if (!hasValidData) {
    alert('请至少填写一个检查项目');
    return;
  }

  // 构造提交数据 - 符合RecordDTO格式
  const environmentItems = [];
  const securityItems = [];

  // 数据中心数据
  if (areaInfo.value.areaType === 'D') {
    // 安全检查项
    if (soundLightAlarm.value !== null) {
      securityItems.push({
        name: '机房范围内是否有声光报警',
        status: soundLightAlarm.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (fireLoadMaterials.value !== null) {
      securityItems.push({
        name: '机房内是否有纸箱、泡沫等火载量',
        status: fireLoadMaterials.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (roomLeakage.value !== null) {
      securityItems.push({
        name: '机房内是否有漏水',
        status: roomLeakage.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (ceilingLeakage.value !== null) {
      securityItems.push({
        name: '天花是否有漏水',
        status: ceilingLeakage.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    
    // 环境检查项
    if (roomTemperature.value !== null) {
      environmentItems.push({
        name: '机房环境温度',
        status: 'normal', // 温度项目默认为正常，具体判断由后端处理
        remark: `${roomTemperature.value}°C`
      });
    }

    // 模块检查项 - 归类为设备检查项，暂时放入securityItems
    moduleData.value.forEach((module, index) => {
      if (module.returnTemp !== null) {
        environmentItems.push({
          name: `模块${index + 1}回风温度`,
          status: 'normal',
          remark: `${module.returnTemp}°C`
        });
      }
      if (module.powerStatus !== null) {
        securityItems.push({
          name: `模块${index + 1}供电状态`,
          status: module.powerStatus ? 'normal' : 'abnormal',
          remark: null
        });
      }
    });
  }
  
  // 弱电间数据
  if (areaInfo.value.areaType === 'E') {
    // 漏水检查 - 归类为安全检查
    if (floorLeakage.value !== null) {
      securityItems.push({
        name: '机房地面是否漏水',
        status: floorLeakage.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (wallLeakage.value !== null) {
      securityItems.push({
        name: '墙壁是否漏水',
        status: wallLeakage.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (ceilingLeakageE.value !== null) {
      securityItems.push({
        name: '天花是否漏水',
        status: ceilingLeakageE.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    
    // 安全检查
    if (combustibleMaterials.value !== null) {
      securityItems.push({
        name: '机房是否有垃圾纸箱等可燃物',
        status: combustibleMaterials.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    
    // 环境检查
    if (roomTemperatureE.value !== null) {
      environmentItems.push({
        name: '机房环境温度',
        status: 'normal',
        remark: `${roomTemperatureE.value}°C`
      });
    }
    
    // 供电检查 - 归类为安全检查
    if (mainsPower.value !== null) {
      securityItems.push({
        name: '市电供电是否正常',
        status: mainsPower.value ? 'normal' : 'abnormal',
        remark: null
      });
    }
    if (upsPower.value !== null) {
      securityItems.push({
        name: 'UPS供电是否正常',
        status: upsPower.value ? 'normal' : 'abnormal',
        remark: null
      });
    }
    
    // 设备检查 - 归类为安全检查
    if (cabinetLock.value !== null) {
      securityItems.push({
        name: '网络机柜是否正常上锁',
        status: cabinetLock.value ? 'normal' : 'abnormal',
        remark: null
      });
    }
    if (accessControl.value !== null) {
      securityItems.push({
        name: '机房门禁是否正常',
        status: accessControl.value ? 'normal' : 'abnormal',
        remark: null
      });
    }
    if (equipmentNoise.value !== null) {
      securityItems.push({
        name: '机房设备运行是否有异响',
        status: equipmentNoise.value ? 'abnormal' : 'normal',
        remark: null
      });
    }
    if (lightingCheck.value !== null) {
      securityItems.push({
        name: '机房照明是否正常',
        status: lightingCheck.value ? 'normal' : 'abnormal',
        remark: null
      });
    }
  }

  // 确保至少有一个环境检查项和一个安全检查项
  if (environmentItems.length === 0) {
    environmentItems.push({
      name: '环境检查',
      status: 'normal',
      remark: '无特殊情况'
    });
  }
  
  if (securityItems.length === 0) {
    securityItems.push({
      name: '安全检查',
      status: 'normal',
      remark: '无特殊情况'
    });
  }

  const payload = {
    areaId: areaInfo.value.id,
    inspectorId: 1, // TODO: 从用户信息获取
    inspectorName: '测试员', // TODO: 从用户信息获取
    startTime: new Date().toISOString(),
    endTime: new Date().toISOString(),
    status: 'normal', // 后端会根据异常项目自动调整
    remark: remark.value,
    environmentItems: environmentItems,
    securityItems: securityItems
  }

  isSubmitting.value = true;
  try {
    const res = await createRecord(payload);
    if (res.code === 200 || res.code === 0) {
      alert('提交成功');
      router.push('/records');
    } else {
      throw new Error(res.message || '提交失败');
    }
  } catch (err) {
    console.error('提交巡检记录失败:', err);
    alert(err.message || '提交失败，请重试');
  } finally {
    isSubmitting.value = false;
  }
}

onMounted(() => {
  loadAreaInfo();
})
</script>

<style scoped>
/* 基础容器样式 */
.form-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 80px;
}

.form-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  background: linear-gradient(135deg, #2196F3, #1976D2);
  color: white;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.form-header h1 {
  font-size: 18px;
  margin: 0;
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-button {
  position: absolute;
  left: 16px;
  color: white;
  text-decoration: none;
  padding: 8px;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.back-button:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.form-content {
  padding: 16px;
  max-width: 600px;
  margin: 0 auto;
}

/* 表单区块样式 */
.form-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.form-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #f0f0f0;
}

/* 表单项样式 */
.form-item {
  margin-bottom: 20px;
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-item span {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

/* 单选按钮组样式 */
.radio-group {
  display: flex;
  gap: 24px;
}

.radio-option {
  display: flex;
  align-items: center;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}

.radio-option input[type="radio"] {
  margin-right: 6px;
  transform: scale(1.1);
}

.radio-option span {
  margin: 0;
  display: inline;
}

/* 输入框样式 */
.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.input-group input:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.1);
}

/* 模块选择器样式 */
.module-selector {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.module-selector label {
  font-weight: 500;
  color: #333;
}

.module-selector select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  background: white;
}

/* 模块组样式 */
.module-group {
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  background: #fafafa;
}

.module-header {
  font-size: 15px;
  font-weight: 600;
  color: #2196F3;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e8e8e8;
}

/* 备注输入框样式 */
.remarks-input {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  font-family: inherit;
}

.remarks-input:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.1);
}

/* 提交按钮样式 */
.form-actions {
  padding: 20px 0;
}

.submit-button {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #2196F3, #1976D2);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.submit-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(33, 150, 243, 0.4);
}

.submit-button:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 加载状态样式 */
.loading-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #2196F3;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .form-content {
    padding: 12px;
  }
  
  .form-section {
    padding: 16px;
  }
  
  .radio-group {
    gap: 16px;
  }
}
</style>