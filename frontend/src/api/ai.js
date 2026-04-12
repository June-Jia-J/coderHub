import { request } from './request'

export function aiChat(data) {
  return request.post('/api/ai/chat', data)
}

export function aiGenerate(data) {
  return request.post('/api/ai/generate', data)
}
