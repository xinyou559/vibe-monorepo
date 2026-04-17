<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
onLaunch(() => {
  console.log("App Launch");
});
onShow(() => {
  console.log("App Show");
});
onHide(() => {
  console.log("App Hide");
});
</script>
<style>
page {
  --bg: #f7f7f9;
  --surface: #ffffff;
  --text: #111111;
  --muted: rgba(17, 17, 17, 0.56);
  --subtle: rgba(17, 17, 17, 0.12);
  --hairline: rgba(17, 17, 17, 0.08);
  --accent: #111111;
  --danger: #e43d33;
  background: var(--bg);
  color: var(--text);
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "Segoe UI", Roboto, Helvetica, Arial,
    "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

.page {
  padding: 32rpx;
}

.title-xl {
  font-size: 44rpx;
  font-weight: 700;
  letter-spacing: -0.6rpx;
  line-height: 1.15;
}

.title-sm {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--muted);
}

.surface {
  background: var(--surface);
  border: 1px solid var(--hairline);
  border-radius: 20rpx;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.btn {
  height: 80rpx;
  line-height: 80rpx;
  border-radius: 999rpx;
  padding: 0 28rpx;
  font-size: 28rpx;
  font-weight: 600;
}

.btn-primary {
  background: var(--accent);
  color: #ffffff;
}

.btn-primary::after {
  border: none;
}

.btn-ghost {
  background: transparent;
  color: var(--text);
  border: 1px solid var(--hairline);
}

.btn-ghost::after {
  border: none;
}

.mono {
  font-variant-numeric: tabular-nums;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", monospace;
}
</style>
