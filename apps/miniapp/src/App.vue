<script setup lang="ts">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'

onLaunch(() => {
  console.log('App Launch')
})
onShow(() => {
  console.log('App Show')
})
onHide(() => {
  console.log('App Hide')
})
</script>

<style>
/* 
  全局设计系统：克制、线框、单一强调色 (Coral & Midnight)
  遵循 frontend-skill 原则，消除大面积块状阴影，改用干净的表面与极简的色彩。
*/
page {
  --bg-canvas: #F7F7F9;
  --bg-surface: #FFFFFF;
  --bg-midnight: #1A1B26;
  --text-primary: #111111;
  --text-secondary: #666666;
  --text-tertiary: #999999;
  --text-inverse: #FFFFFF;
  --border-light: #EAEAEA;
  --accent-color: #FF6B4A; /* 活力珊瑚橙 */
  --accent-hover: #E65A3D;
  --radius-sm: 8rpx;
  --radius-md: 16rpx;
  --radius-lg: 24rpx;

  background-color: var(--bg-canvas);
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
  color: var(--text-primary);
  font-size: 28rpx;
  line-height: 1.5;
}

/* 全局可复用的组件级样式 */
.page-container {
  padding: 30rpx;
  min-height: 100vh;
  box-sizing: border-box;
}

.surface-card {
  background: var(--bg-surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  padding: 30rpx;
  margin-bottom: 24rpx;
}

.text-title {
  font-size: 36rpx;
  font-weight: 600;
  color: var(--text-primary);
}

.text-subtitle {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--text-secondary);
}

.text-caption {
  font-size: 24rpx;
  color: var(--text-tertiary);
}

.text-accent {
  color: var(--accent-color);
  font-weight: 600;
}

.btn-primary {
  background-color: var(--accent-color);
  color: var(--text-inverse);
  border-radius: var(--radius-sm);
  font-size: 30rpx;
  font-weight: 500;
  text-align: center;
  padding: 20rpx 0;
  border: none;
}

.btn-primary:active {
  background-color: var(--accent-hover);
  transform: scale(0.98);
}
</style>
