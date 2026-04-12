<template>
  <section class="blog-edit" aria-label="写博客">
    <h1 class="page-title">{{ isEdit ? '编辑博客' : '写博客' }}</h1>
    <div class="form-card card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="title">
          <el-input v-model="form.title" placeholder="标题" size="large" maxlength="256" show-word-limit />
        </el-form-item>
        <el-form-item prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="正文（支持 Markdown）" :rows="12" />
        </el-form-item>
        <div class="actions">
          <el-button type="primary" :loading="submitting" @click="submit">{{ isEdit ? '保存' : '发布' }}</el-button>
          <el-button type="success" :loading="aiLoading" @click="aiGenerate">AI 帮我写</el-button>
        </div>
      </el-form>
    </div>
    <div class="ai-card card">
      <div class="ai-card-header">
        <img src="/images/ai-assist.svg" alt="" class="ai-card-illustration" />
        <h2 class="card-heading">AI 小巧思 — 和 AI 聊聊</h2>
      </div>
      <AiChat />
    </div>
  </section>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createBlog, updateBlog, getBlogDetail } from '../api/blog'
import { aiGenerate as aiGenerateApi } from '../api/ai'
import { validateTitle, validateContent } from '../utils/validate'
import AiChat from '../components/AiChat.vue'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const submitting = ref(false)
const aiLoading = ref(false)
const form = reactive({ title: '', content: '' })
const isEdit = computed(() => !!route.params.id)

const rules = {
  title: [{ validator: (r, v, c) => { const e = validateTitle(v); e ? c(new Error(e)) : c(); } }],
  content: [{ validator: (r, v, c) => { const e = validateContent(v); e ? c(new Error(e)) : c(); } }]
}

async function submit() {
  const ok = await formRef.value?.validate().catch(() => false)
  if (!ok) return
  submitting.value = true
  try {
    const id = route.params.id
    if (isEdit.value && id) {
      const res = await updateBlog(id, form)
      const d = res?.data ?? res
      ElMessage.success('保存成功')
      router.push(d?.id ? `/blogs/${d.id}` : '/blogs')
    } else {
      const res = await createBlog(form)
      const d = res?.data ?? res
      ElMessage.success('发布成功')
      router.push(d?.id ? `/blogs/${d.id}` : '/blogs')
    }
  } catch (err) {
    const msg = err?.response?.data?.message ?? err?.message ?? (isEdit.value ? '保存失败，请重试' : '发布失败，请重试')
    ElMessage.error(msg)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const id = route.params.id
  if (!id) return
  try {
    const res = await getBlogDetail(id)
    const d = res?.data ?? res
    if (d) {
      form.title = d.content ?? ''
      form.content = d.title ?? ''
    }
  } catch {
    ElMessage.error('加载博客失败')
    router.push('/blogs')
  }
})

async function aiGenerate() {
  if (!form.title?.trim()) {
    ElMessage.warning('请先填写标题')
    return
  }
  aiLoading.value = true
  try {
    const res = await aiGenerateApi({ title: form.title, outline: form.content || undefined })
    const d = res?.data ?? res
    const content = d?.content ?? ''
    if (content) {
      form.content = form.content ? form.content + '\n\n' + content : content
      ElMessage.success('已插入 AI 生成内容')
    }
  } finally {
    aiLoading.value = false
  }
}
</script>

<style scoped>
.blog-edit { padding: 0 0 40px; }
.page-title {
  font-family: var(--font-heading);
  font-size: 1.9rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  margin: 0 0 28px;
  color: var(--text-primary);
  text-shadow: 0 0 12px rgba(13, 148, 136, 0.25);
}
.card {
  background: var(--bg-surface);
  border: 2px solid var(--border-default);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: 28px;
  margin-bottom: 24px;
}
.card:hover { border-color: var(--border-neon); box-shadow: var(--shadow-sm), var(--glow-primary); }
.form-card :deep(.el-form-item) { margin-bottom: 20px; }
.form-card :deep(.el-input__wrapper),
.form-card :deep(.el-textarea__inner) {
  border-radius: var(--radius-md);
  background: var(--bg-base);
  border: 2px solid var(--border-default);
}
.form-card :deep(.el-input__wrapper:hover),
.form-card :deep(.el-input__wrapper.is-focus),
.form-card :deep(.el-textarea__inner:hover),
.form-card :deep(.el-textarea__inner:focus) {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px var(--color-primary-soft), var(--glow-primary);
}
.actions { display: flex; gap: 12px; flex-wrap: wrap; }
.actions .el-button { border-radius: var(--radius-md); font-weight: 500; }
.actions .el-button:hover { transform: translateY(-1px); }
.actions .el-button:active { transform: translateY(0); }
.ai-card-header {
  display: flex;
  align-items: center;
  gap: var(--space-5);
  margin-bottom: 16px;
}
.ai-card-illustration {
  width: 120px;
  height: auto;
  max-height: 84px;
  object-fit: contain;
  flex-shrink: 0;
}
.card-heading {
  font-family: var(--font-heading);
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  letter-spacing: 0.03em;
  color: var(--text-primary);
  text-shadow: 0 0 8px rgba(13, 148, 136, 0.2);
}
@media (max-width: 640px) {
  .page-title { font-size: 1.375rem; margin-bottom: 20px; }
  .card { padding: 20px; }
}
</style>
