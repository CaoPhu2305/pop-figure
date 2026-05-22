import apiClient from '@/api/index'
import type { ApiResponse, AuthResponse, LoginRequest, RegisterRequest } from '@/types'

export type IntrospectRequest = {
  token: string
}

export type LogoutRequest = {
  token: string
}

export type RefreshRequest = {
  token: string
}

export const authApi = {
  login(payload: LoginRequest) {
    return apiClient.post<ApiResponse<AuthResponse>>('/auth/log-in', payload)
  },

  introspect(payload: IntrospectRequest) {
    return apiClient.post<ApiResponse<{ valid: boolean }>>('/auth/introspect', payload)
  },

  logout(payload: LogoutRequest) {
    return apiClient.post<ApiResponse<void>>('/auth/logout', payload)
  },

  refresh(payload: RefreshRequest) {
    return apiClient.post<ApiResponse<AuthResponse>>('/auth/refresh', payload)
  },

  register(payload: RegisterRequest) {
    return apiClient.post<ApiResponse<unknown>>('/users', {
      name: payload.name,
      email: payload.email,
      phone: payload.phone,
      hash_password: payload.password,
    })
  },
}

export default authApi
