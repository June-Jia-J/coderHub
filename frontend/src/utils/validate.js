export function validateUsername(v) {
  if (!v || typeof v !== 'string') return '用户名不能为空'
  const t = v.trim()
  if (t.length < 2 || t.length > 64) return '用户名长度 2-64'
  return ''
}

export function validatePassword(v) {
  if (!v || typeof v !== 'string') return '密码不能为空'
  if (v.length < 6 || v.length > 32) return '密码长度 6-32'
  return ''
}

export function validateTitle(v) {
  if (!v || typeof v !== 'string') return '标题不能为空'
  if (v.trim().length > 256) return '标题最长 256 字'
  return ''
}

export function validateContent(v) {
  if (v == null || typeof v !== 'string') return '正文不能为空'
  if (!v.trim()) return '正文不能为空'
  return ''
}
