import { request } from './request'

export function getBlogList(params = {}) {
  const query = { page: params.page || 1, size: params.size || 10 }
  if (params.author) query.author = params.author
  return request.get('/api/blogs', { params: query })
}

export function getBlogDetail(id) {
  return request.get(`/api/blogs/${id}`)
}

export function createBlog(data) {
  return request.post('/api/blogs', data)
}

export function updateBlog(id, data) {
  return request.put(`/api/blogs/${id}`, data)
}

export function deleteBlog(id) {
  return request.delete(`/api/blogs/${id}`)
}
