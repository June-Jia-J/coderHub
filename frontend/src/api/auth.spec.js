import { describe, it, expect, vi, beforeEach } from 'vitest'
import { login, register, logout } from './auth'
import { request } from './request'

vi.mock('./request', () => ({
  request: {
    post: vi.fn()
  }
}))

describe('Auth API', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login', () => {
    it('should call request.post with credentials', async () => {
      const credentials = { username: 'testuser', password: 'password123' }
      const mockResponse = { data: { token: 'test-token', username: 'testuser' } }
      request.post.mockResolvedValue(mockResponse)

      const result = await login(credentials)

      expect(request.post).toHaveBeenCalledWith('/api/auth/login', credentials)
      expect(result).toEqual(mockResponse)
    })
  })

  describe('register', () => {
    it('should call request.post with user data', async () => {
      const userData = { username: 'newuser', password: 'password123' }
      const mockResponse = { data: null, code: 200 }
      request.post.mockResolvedValue(mockResponse)

      const result = await register(userData)

      expect(request.post).toHaveBeenCalledWith('/api/auth/register', userData)
      expect(result).toEqual(mockResponse)
    })
  })

  describe('logout', () => {
    it('should call request.post to logout endpoint', async () => {
      const mockResponse = { data: null }
      request.post.mockResolvedValue(mockResponse)

      const result = await logout()

      expect(request.post).toHaveBeenCalledWith('/api/auth/logout')
      expect(result).toEqual(mockResponse)
    })

    it('should return empty object on error', async () => {
      request.post.mockRejectedValue(new Error('Network error'))

      const result = await logout()

      expect(result).toEqual({})
    })
  })
})
