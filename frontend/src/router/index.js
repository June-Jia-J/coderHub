import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  { path: '/', redirect: '/blogs' },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { guest: true } },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue'), meta: { guest: true } },
  { path: '/blogs', name: 'BlogList', component: () => import('../views/BlogList.vue') },
  { path: '/blogs/new', name: 'BlogNew', component: () => import('../views/BlogEdit.vue'), meta: { requiresAuth: true } },
  { path: '/blogs/edit/:id', name: 'BlogEdit', component: () => import('../views/BlogEdit.vue'), meta: { requiresAuth: true } },
  { path: '/blogs/:id', name: 'BlogDetail', component: () => import('../views/BlogDetail.vue') },
  { path: '/users/:username', name: 'UserProfile', component: () => import('../views/UserProfile.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.guest && userStore.token && (to.name === 'Login' || to.name === 'Register')) {
    next({ path: '/blogs' })
  } else {
    next()
  }
})

export default router
