<template>
  <header class="app-header" role="banner">
    <router-link to="/blogs" class="logo">
      <img v-show="logoLoaded" src="/images/logo.svg" alt="" class="logo-img" @load="logoLoaded = true" @error="logoLoaded = false" />
      <span class="logo-text">程序员博客</span>
    </router-link>
    <nav class="nav" aria-label="主导航">
      <router-link to="/blogs" class="nav-link">博客列表</router-link>
      <router-link v-if="userStore.token" to="/blogs/new" class="nav-link nav-link-cta">写博客</router-link>
      <template v-if="userStore.token">
        <span class="username">{{ userStore.username }}</span>
        <button type="button" class="btn-logout" @click="handleLogout">退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="nav-link">登录</router-link>
        <router-link to="/register" class="nav-link nav-link-cta">注册</router-link>
      </template>
    </nav>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const logoLoaded = ref(false)

function handleLogout() {
  userStore.logout()
  router.push('/blogs')
}
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 28px;
  background: var(--bg-surface);
  border-bottom: 2px solid var(--border-neon);
  box-shadow: var(--shadow-sm), var(--glow-primary);
  min-height: 52px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
  min-width: 0;
}
.logo-img {
  height: 36px;
  width: auto;
  flex-shrink: 0;
  object-fit: contain;
  transition: filter var(--transition-fast);
}
.logo:hover .logo-img { filter: drop-shadow(0 0 8px rgba(13, 148, 136, 0.8)); }
.logo-text {
  font-family: var(--font-heading);
  font-weight: 700;
  font-size: 1.35rem;
  letter-spacing: 0.05em;
  color: var(--color-primary);
  text-shadow: var(--glow-primary);
  white-space: nowrap;
}
.logo:hover .logo-text { color: var(--color-primary-hover); text-shadow: var(--glow-primary-strong); }
.nav {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-shrink: 0;
}
.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 0.9375rem;
  padding: 6px 12px;
  border-radius: var(--radius-sm);
  transition: color var(--transition-fast), background var(--transition-fast);
  white-space: nowrap;
}
.nav-link:hover { color: var(--text-primary); background: var(--bg-elevated); }
.nav-link:active { background: var(--bg-overlay); }
.nav-link-cta {
  color: var(--color-primary);
  font-weight: 500;
}
.nav-link-cta:hover { color: var(--color-primary-hover); background: var(--color-primary-soft); }
.username {
  color: var(--text-muted);
  font-size: 0.875rem;
  padding: 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 72px;
}
.btn-logout {
  background: none;
  border: none;
  color: var(--text-secondary);
  font-size: 0.9375rem;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: var(--radius-sm);
  transition: color var(--transition-fast), background var(--transition-fast);
  white-space: nowrap;
}
.btn-logout:hover { color: var(--text-primary); background: var(--bg-elevated); }
.btn-logout:active { background: var(--bg-overlay); }
@media (max-width: 640px) {
  .app-header { padding: 12px 16px; gap: 8px; }
  .logo { gap: 8px; }
  .logo-img { height: 30px; }
  .logo-text { display: none; }
  .nav { gap: 8px; }
  .nav-link { padding: 6px 8px; font-size: 0.8125rem; }
  .username { max-width: 56px; font-size: 0.8125rem; }
  .btn-logout { padding: 6px 8px; font-size: 0.8125rem; }
}
</style>
