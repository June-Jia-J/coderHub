import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { ref, nextTick } from 'vue'
import BlogList from './BlogList.vue'

// Mock Element Plus components
vi.mock('element-plus', () => ({
  ElSkeleton: { name: 'ElSkeleton', render: () => null },
  ElEmpty: { name: 'ElEmpty', render: () => null },
  ElPagination: { name: 'ElPagination', render: () => null }
}))

// Mock router-link
vi.mock('vue-router', () => ({
  RouterLink: {
    name: 'RouterLink',
    props: ['to'],
    render() { return null }
  }
}))

// Mock blog API
vi.mock('../api/blog', () => ({
  getBlogList: vi.fn()
}))

import { getBlogList } from '../api/blog'

describe('BlogList View', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('should fetch blog list on mount', async () => {
    const mockBlogs = {
      list: [
        { id: 1, title: 'Blog 1', summary: 'Summary 1', authorName: 'Author 1', createdAt: '2024-01-01' },
        { id: 2, title: 'Blog 2', summary: 'Summary 2', authorName: 'Author 2', createdAt: '2024-01-02' }
      ],
      total: 2
    }
    getBlogList.mockResolvedValue({ data: mockBlogs })

    mount(BlogList, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElPagination']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))

    expect(getBlogList).toHaveBeenCalledWith({ page: 1, size: 10 })
  })

  it('should update list data after fetching', async () => {
    const mockBlogs = {
      list: [
        { id: 1, title: 'Test Blog', summary: 'Test Summary', authorName: 'Test Author', createdAt: '2024-01-01T00:00:00' }
      ],
      total: 1
    }
    getBlogList.mockResolvedValue({ data: mockBlogs })

    const wrapper = mount(BlogList, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElPagination']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    
    // Check that the component's internal state is updated
    expect(wrapper.vm.list).toEqual(mockBlogs.list)
    expect(wrapper.vm.total).toBe(1)
    expect(wrapper.vm.loading).toBe(false)
  })

  it('should show empty state when no blogs', async () => {
    getBlogList.mockResolvedValue({ data: { list: [], total: 0 } })

    const wrapper = mount(BlogList, {
      global: {
        stubs: ['RouterLink', 'ElSkeleton', 'ElEmpty', 'ElPagination']
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))

    expect(wrapper.vm.list).toEqual([])
    expect(wrapper.vm.total).toBe(0)
  })
})
