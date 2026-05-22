import apiClient from '@/api/index'
import type { ApiResponse, CategoryResponse } from '@/types'

export type CategoryPayload = {
  name: string
  slug: string
}

export const categoriesApi = {
  getAll() {
    return apiClient.get<ApiResponse<CategoryResponse[]>>('/categories')
  },

  getById(id: number) {
    return apiClient.get<ApiResponse<CategoryResponse>>(`/categories/${id}`)
  },

  create(payload: CategoryPayload) {
    return apiClient.post<ApiResponse<CategoryResponse>>('/categories', payload)
  },

  update(id: number, payload: CategoryPayload) {
    return apiClient.put<ApiResponse<CategoryResponse>>(`/categories/${id}`, payload)
  },

  delete(id: number) {
    return apiClient.delete<ApiResponse<void>>(`/categories/${id}`)
  },
}

export default categoriesApi
