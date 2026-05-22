import apiClient from '@/api/index'
import type { ApiResponse, ProductResponse } from '@/types'

export type ProductPayload = {
  name: string
  slug: string
  description: string
  isVaulted?: boolean
  categoryId?: number | null
}

export const productsApi = {
  getAll(params?: {
    categoryId?: number
    search?: string
  }) {
    return apiClient.get<ApiResponse<ProductResponse[]>>('/products', { params })
  },

  getById(id: number) {
    return apiClient.get<ApiResponse<ProductResponse>>(`/products/${id}`)
  },

  getByCategory(categoryId: number) {
    return apiClient.get<ApiResponse<ProductResponse[]>>(`/products/category/${categoryId}`)
  },

  create(payload: ProductPayload) {
    return apiClient.post<ApiResponse<ProductResponse>>('/products', payload)
  },

  update(id: number, payload: ProductPayload) {
    return apiClient.put<ApiResponse<ProductResponse>>(`/products/${id}`, payload)
  },

  delete(id: number) {
    return apiClient.delete<ApiResponse<void>>(`/products/${id}`)
  },
}

export default productsApi
