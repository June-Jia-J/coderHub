<template>
  <div class="app-layout">
    <AppHeader />
    <main class="main-content" role="main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import AppHeader from './components/AppHeader.vue'
</script>

<style>
@import './assets/styles/variables.css';

* { box-sizing: border-box; }
html { scroll-behavior: smooth; }
body {
  margin: 0;
  font-family: var(--font-mono), 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: radial-gradient(1200px 600px at 20% -10%, rgba(34, 211, 238, 0.10), transparent 55%),
    radial-gradient(900px 520px at 85% 10%, rgba(13, 148, 136, 0.12), transparent 55%),
    linear-gradient(180deg, var(--bg-grad-1), var(--bg-grad-2) 45%, var(--bg-grad-3));
  color: var(--text-primary);
  line-height: var(--leading-normal);
  -webkit-font-smoothing: antialiased;
  position: relative;
}
/* 赛博朋克：网格 + 扫描线（克制，不影响可读性） */
body::before {
  content: '';
  position: fixed;
  inset: 0;
  background-image:
    linear-gradient(var(--border-grid) 1px, transparent 1px),
    linear-gradient(90deg, var(--border-grid) 1px, transparent 1px);
  background-size: 28px 28px;
  pointer-events: none;
  z-index: 0;
}
.app-layout::before {
  content: '';
  position: fixed;
  inset: 0;
  background: repeating-linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.02) 0px,
    rgba(255, 255, 255, 0.02) 1px,
    transparent 2px,
    transparent 6px
  );
  opacity: 0.35;
  mix-blend-mode: overlay;
  pointer-events: none;
  z-index: 0;
}
.app-layout { min-height: 100vh; display: flex; flex-direction: column; position: relative; z-index: 1; }
.main-content {
  flex: 1;
  max-width: var(--content-wide);
  margin: 0 auto;
  padding: 32px 24px 48px;
  width: 100%;
}
@media (max-width: 640px) {
  .main-content { padding: 20px 16px 32px; }
}
.fade-enter-active, .fade-leave-active { transition: opacity var(--transition-fast); }
.fade-enter-from, .fade-leave-to { opacity: 0; }

/* 全局按钮/输入框 hover、focus 增强（Element 组件通过变量已覆盖主色） */
.el-button--primary:hover { transform: translateY(-1px); }
.el-button--primary:active { transform: translateY(0); }
.el-input__wrapper { transition: box-shadow var(--transition-fast), border-color var(--transition-fast); }
.el-input__wrapper:hover { box-shadow: var(--shadow-sm); }

/* 现代官网：标题更清晰的层级 */
h1, h2, h3 { line-height: var(--leading-tight); }
</style>
