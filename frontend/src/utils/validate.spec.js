import { describe, it, expect } from 'vitest'
import { validateUsername, validatePassword, validateTitle, validateContent } from './validate'

describe('Validate Utils', () => {
  describe('validateUsername', () => {
    it('should return error for empty username', () => {
      expect(validateUsername('')).toBe('用户名不能为空')
      expect(validateUsername(null)).toBe('用户名不能为空')
      expect(validateUsername(undefined)).toBe('用户名不能为空')
    })

    it('should return error for username too short', () => {
      expect(validateUsername('a')).toBe('用户名长度 2-64')
    })

    it('should return error for username too long', () => {
      expect(validateUsername('a'.repeat(65))).toBe('用户名长度 2-64')
    })

    it('should return empty string for valid username', () => {
      expect(validateUsername('validuser')).toBe('')
      expect(validateUsername('user123')).toBe('')
      expect(validateUsername('ab')).toBe('')
    })
  })

  describe('validatePassword', () => {
    it('should return error for empty password', () => {
      expect(validatePassword('')).toBe('密码不能为空')
      expect(validatePassword(null)).toBe('密码不能为空')
    })

    it('should return error for password too short', () => {
      expect(validatePassword('12345')).toBe('密码长度 6-32')
    })

    it('should return error for password too long', () => {
      expect(validatePassword('a'.repeat(33))).toBe('密码长度 6-32')
    })

    it('should return empty string for valid password', () => {
      expect(validatePassword('password123')).toBe('')
      expect(validatePassword('123456')).toBe('')
    })
  })

  describe('validateTitle', () => {
    it('should return error for empty title', () => {
      expect(validateTitle('')).toBe('标题不能为空')
      expect(validateTitle(null)).toBe('标题不能为空')
    })

    it('should return error for title too long', () => {
      expect(validateTitle('a'.repeat(257))).toBe('标题最长 256 字')
    })

    it('should return empty string for valid title', () => {
      expect(validateTitle('Valid Title')).toBe('')
    })
  })

  describe('validateContent', () => {
    it('should return error for empty content', () => {
      expect(validateContent('')).toBe('正文不能为空')
      expect(validateContent(null)).toBe('正文不能为空')
    })

    it('should return error for whitespace only content', () => {
      expect(validateContent('   ')).toBe('正文不能为空')
    })

    it('should return empty string for valid content', () => {
      expect(validateContent('This is valid content')).toBe('')
    })
  })
})
