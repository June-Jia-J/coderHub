<template>
  <section class="user-profile" aria-label="用户主页">
    <h1 class="page-title">{{ username ? `${username} 的博客` : '用户博客' }}</h1>
    <el-skeleton v-if="loading" :rows="5" animated class="skeleton" />
    <template v-else>
      <el-empty v-if="!list.length" description="该用户暂无博客" class="empty" />
      <ul v-else class="list" role="list">
        <li v-for="item in list" :key="item.id">
          <router-link :to="`/blogs/${item.id}`" class="card">
            <h2 class="card-title">{{ item.title }}</h2>
            <p class="card-summary">{{ item.summary }}</p>
            <div class="card-meta">{{ item.authorName }} · {{ formatDate(item.createdAt) }}</div>
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
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getBlogList } from '../api/blog'

const route = useRoute()
const username = computed(() => route.params.username ? decodeURIComponent(route.params.username) : '')
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
  if (!username.value) return
  loading.value = true
  try {
    const res = await getBlogList({ page: page.value, size: size.value, author: username.value })
    const d = res?.data ?? res
    list.value = d?.list ?? []
    total.value = d?.total ?? 0
  } finally {
    loading.value = false
  }
}

onMounted(fetchList)
watch(username, () => { page.value = 1; fetchList() })
</script>

<style scoped>
.user-profile { padding: 0 0 32px; }
.page-title {
  font-family: var(--font-heading);
  font-size: 1.9rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  margin: 0 0 28px;
  color: var(--text-primary);
  text-shadow: 0 0 12px rgba(13, 148, 136, 0.3);
}
.skeleton { padding: 8px 0; }
.empty { padding: 48px 0; }
.list { list-style: none; margin: 0; padding: 0; display: flex; flex-direction: column; gap: 20px; }
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
.card-title {
  font-family: var(--font-heading);
  margin: 0 0 10px;
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
  letter-spacing: 0.02em;
}
.card-summary { margin: 0 0 12px; color: var(--text-secondary); font-size: 0.9375rem; line-height: 1.55; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.card-meta { font-size: 0.8125rem; color: var(--text-muted); }
.pagination-wrap { margin-top: 32px; display: flex; justify-content: center; }
.pagination-wrap :deep(.el-pagination.is-background .el-pager li) { background: var(--bg-surface); color: var(--text-secondary); }
.pagination-wrap :deep(.el-pagination.is-background .el-pager li.is-active) { background: var(--color-primary); color: #fff; }
@media (max-width: 640px) {
  .page-title { font-size: 1.375rem; margin-bottom: 20px; }
  .card { padding: 18px; }
  .card-title { font-size: 1rem; }
}
</style>
