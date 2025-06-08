<template>
  <div class="form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="router.push('/scan')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>机房巡检 - {{ areaName }}</h1>
    </div>
    <div class="form-content">
      <div class="form-section">
        <div class="form-title">环境巡检</div>
        <div class="form-item">
          <span>机房温度</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="temperature" value="normal" v-model="form.temperature">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="temperature" value="abnormal" v-model="form.temperature">
              <span>异常</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <span>冷通道回风温度</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="cold_channel_temperature" value="normal" v-model="form.cold_channel_temperature">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="cold_channel_temperature" value="abnormal" v-model="form.cold_channel_temperature">
              <span>异常</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <span>供电情况</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="power_supply" value="normal" v-model="form.power_supply">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="power_supply" value="abnormal" v-model="form.power_supply">
              <span>异常</span>
            </label>
          </div>
        </div>
      </div>
      <div class="form-section">
        <div class="form-title">安全巡检</div>
        <div class="form-item">
          <span>门禁状态</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="door_access" value="normal" v-model="form.door_access">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="door_access" value="abnormal" v-model="form.door_access">
              <span>异常</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <span>声光报警</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="sound_light_alarm" value="normal" v-model="form.sound_light_alarm">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="sound_light_alarm" value="abnormal" v-model="form.sound_light_alarm">
              <span>异常</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <span>漏水检测</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="leakage_detection" value="normal" v-model="form.leakage_detection">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="leakage_detection" value="abnormal" v-model="form.leakage_detection">
              <span>异常</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <span>易燃物检查</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" name="flammable_object_check" value="normal" v-model="form.flammable_object_check">
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" name="flammable_object_check" value="abnormal" v-model="form.flammable_object_check">
              <span>异常</span>
            </label>
          </div>
        </div>
      </div>
      <div class="form-section">
        <div class="form-title">备注信息</div>
        <div class="form-item">
          <textarea class="remarks-input" v-model="form.remarks" placeholder="请输入备注信息"></textarea>
        </div>
      </div>
      <div class="form-actions">
        <button class="submit-button" @click="handleSubmit">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()

// 动态区域名，优先取路由参数，否则显示"未识别区域"
const areaName = computed(() => route.query.area || '未识别区域')

const form = ref({
  temperature: '',
  cold_channel_temperature: '',
  power_supply: '',
  door_access: '',
  sound_light_alarm: '',
  leakage_detection: '',
  flammable_object_check: '',
  remarks: ''
})

const handleSubmit = () => {
  // TODO: 可扩展为本地存储或API提交
  alert('提交成功！')
  router.push('/records')
}
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/inspection-form.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');
</style> 