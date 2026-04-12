import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

const TOKEN_KEY = 'blog_token'
const USERNAME_KEY = 'blog_username'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const username = ref(localStorage.getItem(USERNAME_KEY) || '')

  watch(token, (v) => {
    if (v) localStorage.setItem(TOKEN_KEY, v)
    else localStorage.removeItem(TOKEN_KEY)
  }, { immediate: true })

  watch(username, (v) => {
    if (v) localStorage.setItem(USERNAME_KEY, v)
    else localStorage.removeItem(USERNAME_KEY)
  }, { immediate: true })

  function setLogin(t, u) {
    token.value = t || ''
    username.value = u || ''
  }

  function logout() {
    token.value = ''
    username.value = ''
  }

  return { token, username, setLogin, logout }
})
