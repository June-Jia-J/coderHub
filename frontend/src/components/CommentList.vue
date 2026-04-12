<template>
  <section class="comment-list" aria-label="评论">
    <h3 class="comment-heading">评论 ({{ list.length }})</h3>
    <div v-if="userStore.token" class="comment-form">
      <el-input v-model="content" type="textarea" :rows="3" placeholder="写下你的评论…" maxlength="1000" show-word-limit />
      <el-button type="primary" :loading="submitting" class="submit-btn" @click="submit">发表</el-button>
    </div>
    <p v-else class="comment-login-hint">
      <router-link to="/login">登录</router-link> 后参与评论
    </p>
    <ul v-if="list.length" class="list">
      <li v-for="c in list" :key="c.id" class="item">
        <span class="item-author">{{ c.authorName }}</span>
        <span class="item-time">{{ formatDate(c.createdAt) }}</span>
        <p class="item-content">{{ c.content }}</p>
      </li>
    </ul>
    <p v-else-if="!loading" class="empty">暂无评论，来抢沙发吧～</p>
    <el-skeleton v-if="loading" :rows="2" animated />
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getComments, createComment } from '../api/comment'

const props = defineProps({ blogId: { type: [String, Number], required: true } })
const userStore = useUserStore()
const list = ref([])
const loading = ref(true)
const submitting = ref(false)
const content = ref('')

function formatDate(v) {
  if (!v) return ''
  return new Date(v).toLocaleString('zh-CN')
}

async function load() {
  loading.value = true
  try {
    const res = await getComments(props.blogId)
    const d = res?.data ?? res
    list.value = Array.isArray(d) ? d : (d?.list ?? []) || []
  } finally {
    loading.value = false
  }
}

async function submit() {
  const text = content.value?.trim()
  if (!text) {
    ElMessage.warning('请输入评论内容')
    return
  }
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  submitting.value = true
  try {
    const res = await createComment(props.blogId, { content: text })
    const d = res?.data ?? res
    if (d) list.value.push(d)
    content.value = ''
    ElMessage.success('评论成功')
  } catch (err) {
    const msg = err?.response?.data?.message ?? err?.message ?? '评论失败'
    ElMessage.error(msg)
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.comment-list { margin-top: 32px; padding-top: 24px; border-top: 1px solid var(--border-default); }
.comment-heading { font-family: var(--font-heading); font-size: 1.125rem; margin: 0 0 16px; color: var(--text-primary); }
.comment-form :deep(.el-textarea__inner) { border-radius: var(--radius-md); background: var(--bg-base); border: 2px solid var(--border-default); }
.submit-btn { margin-top: 10px; }
.comment-login-hint { color: var(--text-muted); font-size: var(--text-sm); }
.comment-login-hint a { color: var(--color-primary); }
.list { list-style: none; margin: 16px 0 0; padding: 0; }
.item { margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px solid var(--border-subtle); }
.item:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
.item-author { font-weight: 600; color: var(--color-primary); margin-right: 8px; }
.item-time { font-size: var(--text-xs); color: var(--text-muted); }
.item-content { margin: 8px 0 0; color: var(--text-primary); line-height: 1.5; white-space: pre-wrap; word-break: break-word; }
.empty { color: var(--text-muted); font-size: var(--text-sm); margin: 16px 0 0; }
</style>
