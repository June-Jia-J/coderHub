<template>
  <div class="ai-chat">
    <div class="messages">
      <div v-for="(m, i) in messages" :key="i" :class="['msg', m.role]">
        <span class="label">{{ m.role === 'user' ? '我' : 'AI' }}</span>
        <div class="bubble">{{ m.content }}</div>
      </div>
    </div>
    <div class="input-row">
      <el-input v-model="input" type="textarea" :rows="2" placeholder="输入消息与 AI 对话…" @keydown.ctrl.enter="send" />
      <el-button type="primary" :loading="loading" class="send-btn" @click="send">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { aiChat } from '../api/ai'

const messages = ref([])
const input = ref('')
const loading = ref(false)

async function send() {
  const text = input.value?.trim()
  if (!text || loading.value) return
  messages.value.push({ role: 'user', content: text })
  input.value = ''
  loading.value = true
  try {
    const history = messages.value.slice(0, -1).slice(-10).map((m) => ({ role: m.role, content: m.content }))
    const res = await aiChat({ message: text, history })
    const d = res?.data ?? res
    const reply = d?.reply ?? ''
    messages.value.push({ role: 'assistant', content: reply || '（无回复）' })
  } catch {
    messages.value.push({ role: 'assistant', content: '请求失败，请稍后重试。' })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.ai-chat { padding: 8px 0; }
.messages { max-height: 320px; overflow-y: auto; margin-bottom: 16px; padding-right: 4px; }
.messages::-webkit-scrollbar { width: 6px; }
.messages::-webkit-scrollbar-thumb { background: var(--border-default); border-radius: 3px; }
.msg { margin-bottom: 14px; }
.msg.user .bubble {
  background: var(--color-primary);
  color: #fff;
  margin-left: 0;
  margin-right: 48px;
  border-radius: var(--radius-md) var(--radius-md) 4px var(--radius-md);
  box-shadow: var(--glow-primary);
}
.msg.assistant .bubble {
  background: var(--bg-elevated);
  border: 2px solid var(--border-default);
  margin-left: 48px;
  margin-right: 0;
  border-radius: var(--radius-md) var(--radius-md) var(--radius-md) 4px;
}
.label { font-size: 0.75rem; color: var(--text-muted); display: block; margin-bottom: 4px; }
.bubble {
  padding: 12px 16px;
  border-radius: var(--radius-md);
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.5;
  transition: box-shadow var(--transition-fast);
}
.msg.user .bubble:hover { box-shadow: var(--shadow-sm); }
.input-row { display: flex; gap: 12px; align-items: flex-end; }
.input-row :deep(.el-input) { flex: 1; }
.input-row :deep(.el-textarea__inner) {
  border-radius: var(--radius-md);
  background: var(--bg-base);
  border: 2px solid var(--border-default);
}
.input-row :deep(.el-textarea__inner:focus) {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px var(--color-primary-soft), var(--glow-primary);
}
.input-row :deep(.el-textarea__inner:focus) {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px var(--color-primary-soft);
}
.send-btn { border-radius: var(--radius-md); font-weight: 500; flex-shrink: 0; }
.send-btn:hover { transform: translateY(-1px); }
.send-btn:active { transform: translateY(0); }
@media (max-width: 640px) {
  .msg.user .bubble { margin-right: 24px; }
  .msg.assistant .bubble { margin-left: 24px; }
}
</style>
