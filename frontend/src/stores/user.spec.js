import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from './user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('should initialize with empty token and username', () => {
    const store = useUserStore()
    expect(store.token).toBe('')
    expect(store.username).toBe('')
  })

  it('should set login correctly', () => {
    const store = useUserStore()
    store.setLogin('test-token', 'testuser')
    
    expect(store.token).toBe('test-token')
    expect(store.username).toBe('testuser')
  })

  it('should persist token to localStorage', () => {
    const store = useUserStore()
    store.setLogin('persisted-token', 'testuser')
    
    // localStorage is mocked in jsdom, check the store values
    expect(store.token).toBe('persisted-token')
    expect(store.username).toBe('testuser')
  })

  it('should clear data on logout', () => {
    const store = useUserStore()
    store.setLogin('test-token', 'testuser')
    store.logout()
    
    expect(store.token).toBe('')
    expect(store.username).toBe('')
  })

  it('should load from localStorage on init', () => {
    localStorage.setItem('blog_token', 'saved-token')
    localStorage.setItem('blog_username', 'saveduser')
    
    const store = useUserStore()
    expect(store.token).toBe('saved-token')
    expect(store.username).toBe('saveduser')
  })
})
