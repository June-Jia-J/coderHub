<template>
  <section class="register-page" aria-label="注册">
    <div class="card">
      <h2 class="card-title">注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" class="form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="submit-btn" @click="submit">注册</el-button>
        </el-form-item>
      </el-form>
      <p class="footer">
        已有账号？ <router-link to="/login">去登录</router-link>
      </p>
    </div>
  </section>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/auth'
import { validateUsername, validatePassword } from '../utils/validate'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const rules = {
  username: [{ validator: (r, v, c) => { const e = validateUsername(v); e ? c(new Error(e)) : c(); } }],
  password: [{ validator: (r, v, c) => { const e = validatePassword(v); e ? c(new Error(e)) : c(); } }]
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  loading.value = true
  try {
    await register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}
.card {
  width: 100%;
  max-width: 400px;
  padding: 36px 32px;
  background: var(--bg-surface);
  border: 2px solid var(--border-default);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-md);
}
.card:hover { border-color: var(--border-neon); box-shadow: var(--shadow-md), var(--glow-primary); }
.card-title {
  font-family: var(--font-heading);
  margin: 0 0 24px;
  font-size: 1.65rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: var(--text-primary);
  text-align: center;
  text-shadow: 0 0 12px rgba(13, 148, 136, 0.25);
}
.form :deep(.el-form-item) { margin-bottom: 20px; }
.form :deep(.el-input__wrapper) {
  border-radius: var(--radius-md);
  background: var(--bg-base);
  border: 1px solid var(--border-default);
}
.form :deep(.el-input__wrapper:hover),
.form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px var(--color-primary-soft), var(--glow-primary);
}
.submit-btn { width: 100%; border-radius: var(--radius-md); font-weight: 500; }
.footer {
  text-align: center;
  color: var(--text-secondary);
  font-size: 0.9375rem;
  margin: 20px 0 0;
}
.footer a {
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
  transition: color var(--transition-fast);
}
.footer a:hover { color: var(--color-primary-hover); }
@media (max-width: 640px) {
  .register-page { padding: 24px 16px; }
  .card { padding: 28px 24px; }
}
</style>
