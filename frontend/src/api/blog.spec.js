import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getBlogList, getBlogDetail, createBlog, updateBlog, deleteBlog } from './blog'
import { request } from './request'

vi.mock('./request', () => ({
  request: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn()
  }
}))

describe('Blog API', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getBlogList', () => {
    it('should call request.get with correct params', async () => {
      const mockResponse = { data: { list: [], total: 0 } }
      request.get.mockResolvedValue(mockResponse)

      const result = await getBlogList({ page: 2, size: 20 })

      expect(request.get).toHaveBeenCalledWith('/api/blogs', {
        params: { page: 2, size: 20 }
      })
      expect(result).toEqual(mockResponse)
    })

    it('should include author param when provided', async () => {
      const mockResponse = { data: { list: [], total: 0 } }
      request.get.mockResolvedValue(mockResponse)

      await getBlogList({ page: 1, size: 10, author: 'testuser' })

      expect(request.get).toHaveBeenCalledWith('/api/blogs', {
        params: { page: 1, size: 10, author: 'testuser' }
      })
    })

    it('should use default pagination values', async () => {
      const mockResponse = { data: { list: [], total: 0 } }
      request.get.mockResolvedValue(mockResponse)

      await getBlogList({})

      expect(request.get).toHaveBeenCalledWith('/api/blogs', {
        params: { page: 1, size: 10 }
      })
    })
  })

  describe('getBlogDetail', () => {
    it('should call request.get with blog id', async () => {
      const mockResponse = { data: { id: 1, title: 'Test' } }
      request.get.mockResolvedValue(mockResponse)

      const result = await getBlogDetail(1)

      expect(request.get).toHaveBeenCalledWith('/api/blogs/1')
      expect(result).toEqual(mockResponse)
    })
  })

  describe('createBlog', () => {
    it('should call request.post with blog data', async () => {
      const blogData = { title: 'New Blog', content: 'Content' }
      const mockResponse = { data: { id: 1, ...blogData } }
      request.post.mockResolvedValue(mockResponse)

      const result = await createBlog(blogData)

      expect(request.post).toHaveBeenCalledWith('/api/blogs', blogData)
      expect(result).toEqual(mockResponse)
    })
  })

  describe('updateBlog', () => {
    it('should call request.put with id and data', async () => {
      const blogData = { title: 'Updated Blog', content: 'Updated Content' }
      const mockResponse = { data: { id: 1, ...blogData } }
      request.put.mockResolvedValue(mockResponse)

      const result = await updateBlog(1, blogData)

      expect(request.put).toHaveBeenCalledWith('/api/blogs/1', blogData)
      expect(result).toEqual(mockResponse)
    })
  })

  describe('deleteBlog', () => {
    it('should call request.delete with blog id', async () => {
      const mockResponse = { data: null }
      request.delete.mockResolvedValue(mockResponse)

      const result = await deleteBlog(1)

      expect(request.delete).toHaveBeenCalledWith('/api/blogs/1')
      expect(result).toEqual(mockResponse)
    })
  })
})
