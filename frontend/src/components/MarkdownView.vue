<template>
  <div class="markdown-view" v-html="html"></div>
</template>

<script setup>
import { computed } from 'vue'
import { marked } from 'marked'

const props = defineProps({
  content: { type: String, default: '' }
})

const html = computed(() => {
  if (!props.content) return ''
  return marked(props.content, { gfm: true })
})
</script>

<style scoped>
.markdown-view {
  line-height: 1.7;
  word-break: break-word;
}
.markdown-view :deep(pre) {
  background: var(--bg-surface);
  padding: 16px;
  border-radius: var(--radius-md);
  overflow-x: auto;
  border: 1px solid var(--border-default);
}
.markdown-view :deep(code) {
  background: var(--bg-overlay);
  padding: 3px 8px;
  border-radius: var(--radius-sm);
  font-size: 0.9em;
}
.markdown-view :deep(pre code) { background: none; padding: 0; }
.markdown-view :deep(h1) {
  font-size: 1.5rem;
  font-weight: 700;
  border-bottom: 1px solid var(--border-default);
  padding-bottom: 10px;
  margin: 28px 0 16px;
}
.markdown-view :deep(h2) { font-size: 1.25rem; font-weight: 600; margin: 24px 0 12px; }
.markdown-view :deep(h3) { font-size: 1.1rem; margin: 20px 0 10px; }
.markdown-view :deep(ul) { padding-left: 24px; margin: 12px 0; }
.markdown-view :deep(ol) { padding-left: 24px; margin: 12px 0; }
.markdown-view :deep(p) { margin: 12px 0; }
.markdown-view :deep(a) {
  color: var(--color-primary);
  text-decoration: none;
  transition: color var(--transition-fast);
}
.markdown-view :deep(a:hover) { color: var(--color-primary-hover); }
.markdown-view :deep(blockquote) {
  border-left: 4px solid var(--color-primary);
  margin: 16px 0;
  padding: 8px 16px;
  background: var(--color-primary-soft);
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
  color: var(--text-secondary);
}
</style>
