import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { ref } from 'vue'
import BlogDetail from './BlogDetail.vue'
import { useUserStore } from '../stores/user'

// Mock Element Plus
vi.mock('element-plus', () => ({
  ElSkeleton: { name: 'ElSkeleton', render: () => null },
  ElEmpty: { name: 'ElEmpty', render: () => null },
  ElButton: { name: 'ElButton', render: () => null },
  ElMessage: { error: vi.fn(), success: vi.fn() },
  ElMessageBox: { confirm: vi.fn() }
}))

// Mock vue-router
const mockPush = vi.fn()
vi.mock('vue-router', () => ({
  useRoute: () => ({ params: { id: '1' } }),
  useRouter: () => ({ push: mockPush }),
  RouterLink: {
    name: 'RouterLink',
    props: ['to'],
    render() { return null }
  }
}))

// Mock blog API
vi.mock('../api/blog', () => ({
  getBlogDetail: vi.fn(),
  deleteBlog: vi.fn()
}))

import { getBlogDetail, deleteBlog } from '../api/blog'

describe('BlogDetail View', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('should fetch blog detail on mount', async () => {
    const mockBlog = {
      id: 1,
      title: 'Test Blog',
      content: 'Test Content',
      authorName: 'Test Author',
      createdAt: '2024-01-01T00:00:00'
    }
    getBlogDetail.mockResolvedValue({ data: mockBlog })

    mount(BlogDetail, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElButton', 'MarkdownView', 'CommentList']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))

    expect(getBlogDetail).toHaveBeenCalledWith('1')
  })

  it('should show edit/delete buttons for author', async () => {
    const store = useUserStore()
    store.setLogin('test-token', 'Test Author')

    const mockBlog = {
      id: 1,
      title: 'Test Blog',
      content: 'Test Content',
      authorName: 'Test Author',
      createdAt: '2024-01-01T00:00:00'
    }
    getBlogDetail.mockResolvedValue({ data: mockBlog })

    const wrapper = mount(BlogDetail, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElButton', 'MarkdownView', 'CommentList']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.isAuthor).toBe(true)
  })

  it('should not show edit/delete buttons for non-author', async () => {
    const store = useUserStore()
    store.setLogin('test-token', 'Different User')

    const mockBlog = {
      id: 1,
      title: 'Test Blog',
      content: 'Test Content',
      authorName: 'Test Author',
      createdAt: '2024-01-01T00:00:00'
    }
    getBlogDetail.mockResolvedValue({ data: mockBlog })

    const wrapper = mount(BlogDetail, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElButton', 'MarkdownView', 'CommentList']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.isAuthor).toBe(false)
  })
})
