<template>
  <article class="blog-detail" v-if="blog || loading">
    <el-skeleton v-if="loading" :rows="8" animated class="skeleton" />
    <template v-else-if="blog">
      <header class="detail-header">
        <h1 class="detail-title">{{ blog.title }}</h1>
        <p class="detail-meta">
          <router-link v-if="blog.authorName" :to="`/users/${encodeURIComponent(blog.authorName)}`" class="author-link">{{ blog.authorName }}</router-link>
          <span v-else>{{ blog.authorName }}</span>
          · {{ formatDate(blog.createdAt) }}
        </p>
        <div v-if="isAuthor" class="detail-actions">
          <el-button type="primary" size="small" @click="goEdit">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete">删除</el-button>
        </div>
      </header>
      <div class="detail-content">
        <MarkdownView :content="blog.content" />
      </div>
      <CommentList v-if="blog.id" :blog-id="blog.id" />
    </template>
  </article>
  <section v-else class="blog-detail empty-state" aria-label="空状态">
    <el-empty description="博客不存在或已删除" />
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getBlogDetail, deleteBlog } from '../api/blog'
import MarkdownView from '../components/MarkdownView.vue'
import CommentList from '../components/CommentList.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const blog = ref(null)
const isAuthor = computed(() => blog.value && userStore.username && blog.value.authorName === userStore.username)

function formatDate(v) {
  if (!v) return ''
  return new Date(v).toLocaleString('zh-CN')
}

function goEdit() {
  if (blog.value?.id) router.push(`/blogs/edit/${blog.value.id}`)
}

function handleDelete() {
  if (!blog.value?.id) return
  ElMessageBox.confirm('确定要删除这篇博客吗？删除后无法恢复。', '确认删除', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return deleteBlog(blog.value.id)
  }).then(() => {
    ElMessage.success('已删除')
    router.push('/blogs')
  }).catch((err) => {
    if (err !== 'cancel') {
      const msg = err?.response?.data?.message ?? err?.message ?? '删除失败'
      ElMessage.error(msg)
    }
  })
}

onMounted(async () => {
  const id = route.params.id
  if (!id) return
  try {
    const res = await getBlogDetail(id)
    const d = res?.data ?? res
    blog.value = d
  } catch (err) {
    blog.value = null
    const msg = err?.response?.data?.message ?? err?.message ?? '加载博客失败'
    ElMessage.error(msg)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.blog-detail { padding: 0 0 40px; }
.skeleton { padding: 8px 0; }
.detail-header { margin-bottom: 28px; }
.detail-title {
  font-family: var(--font-heading);
  font-size: 1.875rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  line-height: 1.3;
  margin: 0 0 12px;
  color: var(--text-primary);
  text-shadow: 0 0 12px rgba(13, 148, 136, 0.2);
}
.detail-meta { color: var(--text-secondary); font-size: 0.9375rem; margin: 0; }
.author-link { color: var(--color-primary); text-decoration: none; }
.author-link:hover { text-decoration: underline; }
.detail-actions { margin-top: 12px; display: flex; gap: 8px; }
.detail-content { color: var(--text-primary); margin-bottom: 32px; }
.empty-state { padding: 48px 0; }
@media (max-width: 640px) {
  .detail-title { font-size: 1.375rem; }
  .detail-header { margin-bottom: 20px; }
}
</style>
