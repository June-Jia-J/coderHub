import { request } from './request'

export function getComments(blogId) {
  return request.get(`/api/blogs/${blogId}/comments`)
}

export function createComment(blogId, data) {
  return request.post(`/api/blogs/${blogId}/comments`, data)
}
