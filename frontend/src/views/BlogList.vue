<template>
  <section class="blog-list" aria-label="博客列表">
    <header class="hero" aria-label="页面头图">
      <div class="hero-left">
        <p class="hero-kicker">PIXEL · CYBER · HACKER</p>
        <h1 class="page-title">程序员交流博客</h1>
        <p class="hero-subtitle">
          用更接近终端的方式写作与交流：发布、浏览、用 AI 点亮灵感。
        </p>
        <div class="hero-actions">
          <router-link to="/blogs/new" class="hero-cta">开始写博客</router-link>
          <a class="hero-link" href="#list">查看最新博客</a>
        </div>
      </div>
      <div class="hero-right" aria-hidden="true">
        <img class="hero-banner" src="/images/hero-banner.svg" alt="" />
      </div>
    </header>

    <h2 class="section-title" id="list">最新博客</h2>
    <el-skeleton v-if="loading" :rows="5" animated class="skeleton" />
    <template v-else>
      <el-empty v-if="!list.length" image="/images/empty-blog.svg" description="暂无博客" class="empty" />
      <ul v-else class="list" role="list">
        <li v-for="item in list" :key="item.id">
          <router-link :to="`/blogs/${item.id}`" class="card">
            <h2 class="card-title">{{ item.title }}</h2>
            <p class="card-summary">{{ item.summary }}</p>
            <div class="card-meta">
          <router-link v-if="item.authorName" :to="`/users/${encodeURIComponent(item.authorName)}`" class="card-author">{{ item.authorName }}</router-link>
          <span v-else>{{ item.authorName }}</span>
          · {{ formatDate(item.createdAt) }}
        </div>
          </router-link>
        </li>
      </ul>
      <div v-if="total > size" class="pagination-wrap">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next"
          background
          @current-change="fetchList"
        />
      </div>
    </template>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBlogList } from '../api/blog'

const loading = ref(true)
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

function formatDate(v) {
  if (!v) return ''
  const d = new Date(v)
  return d.toLocaleDateString('zh-CN') + ' ' + d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

async function fetchList() {
  loading.value = true
  try {
    const res = await getBlogList({ page: page.value, size: size.value })
    const d = res?.data ?? res
    list.value = d?.list ?? []
    total.value = d?.total ?? 0
  } finally {
    loading.value = false
  }
}

onMounted(fetchList)
</script>

<style scoped>
.blog-list { padding: 0 0 32px; }
.hero {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 24px;
  padding: 28px;
  margin: 0 0 22px;
  background: linear-gradient(180deg, rgba(13, 148, 136, 0.08), rgba(34, 211, 238, 0.04));
  border: 2px solid var(--border-default);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-glow);
  position: relative;
  overflow: hidden;
}
.hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background:
    radial-gradient(500px 240px at 20% 10%, rgba(13, 148, 136, 0.22), transparent 60%),
    radial-gradient(520px 260px at 90% 20%, rgba(34, 211, 238, 0.18), transparent 60%);
  pointer-events: none;
  opacity: 0.65;
}
.hero-left { position: relative; z-index: 1; }
.hero-kicker {
  margin: 0 0 8px;
  font-size: var(--text-sm);
  color: var(--color-accent);
  letter-spacing: 0.18em;
  text-transform: uppercase;
  text-shadow: var(--glow-accent);
}
.page-title {
  font-family: var(--font-heading);
  font-size: 2.4rem;
  font-weight: 700;
  letter-spacing: 0.06em;
  margin: 0 0 10px;
  color: var(--text-primary);
  text-shadow: var(--glow-primary-strong);
}
.hero-subtitle {
  margin: 0 0 16px;
  color: var(--text-secondary);
  font-size: var(--text-lg);
  max-width: 44ch;
}
.hero-actions { display: flex; gap: 14px; align-items: center; flex-wrap: wrap; }
.hero-cta {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  padding: 0 16px;
  border-radius: var(--radius-md);
  background: var(--color-primary);
  color: #fff;
  text-decoration: none;
  box-shadow: var(--glow-primary);
  border: 2px solid rgba(255, 255, 255, 0.06);
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
}
.hero-cta:hover { transform: translateY(-1px); box-shadow: var(--glow-primary-strong); }
.hero-link {
  color: var(--text-secondary);
  text-decoration: none;
  padding: 6px 10px;
  border-radius: var(--radius-sm);
  border: 1px solid transparent;
  transition: color var(--transition-fast), border-color var(--transition-fast), background var(--transition-fast);
}
.hero-link:hover {
  color: var(--text-primary);
  border-color: var(--border-neon);
  background: rgba(13, 148, 136, 0.06);
}
.hero-right { position: relative; z-index: 1; display: flex; align-items: center; justify-content: center; }
.hero-banner { width: 100%; max-width: 340px; height: auto; filter: drop-shadow(0 10px 22px rgba(0,0,0,0.55)); opacity: 0.95; }

.section-title {
  font-family: var(--font-heading);
  font-size: var(--text-xl);
  letter-spacing: 0.06em;
  margin: 0 0 16px;
  color: var(--text-primary);
  text-shadow: 0 0 10px rgba(13, 148, 136, 0.18);
}
.skeleton { padding: 8px 0; }
.empty { padding: 48px 0; }
.list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.card {
  display: block;
  padding: 24px;
  background: var(--bg-surface);
  border: 2px solid var(--border-default);
  border-radius: var(--radius-lg);
  text-decoration: none;
  color: inherit;
  box-shadow: var(--shadow-sm);
  transition: border-color var(--transition-normal), box-shadow var(--transition-normal), transform var(--transition-normal);
}
.card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-md), var(--glow-primary);
  transform: translateY(-2px);
}
.card:active {
  transform: translateY(0);
  box-shadow: var(--shadow-sm);
}
.card-title {
  font-family: var(--font-heading);
  margin: 0 0 10px;
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
  letter-spacing: 0.02em;
}
.card-summary {
  margin: 0 0 12px;
  color: var(--text-secondary);
  font-size: 0.9375rem;
  line-height: 1.55;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.card-meta { font-size: 0.8125rem; color: var(--text-muted); }
.card-author { color: var(--color-primary); text-decoration: none; }
.card-author:hover { text-decoration: underline; }
.pagination-wrap {
  margin-top: 32px;
  display: flex;
  justify-content: center;
}
.pagination-wrap :deep(.el-pagination.is-background .el-pager li) {
  background: var(--bg-surface);
  color: var(--text-secondary);
}
.pagination-wrap :deep(.el-pagination.is-background .el-pager li.is-active) {
  background: var(--color-primary);
  color: #fff;
}
@media (max-width: 640px) {
  .hero { grid-template-columns: 1fr; padding: 20px; }
  .page-title { font-size: 2rem; }
  .card { padding: 18px; }
  .card-title { font-size: 1rem; }
}
</style>
