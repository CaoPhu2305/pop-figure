import apiClient from '@/api/index'
import type { ApiResponse, OrderResponse, OrderStatusRequest } from '@/types'

export const ordersApi = {
  getMyOrders() {
    return apiClient.get<ApiResponse<OrderResponse[]>>('/orders/my')
  },

  getAllOrders() {
    return apiClient.get<ApiResponse<OrderResponse[]>>('/orders')
  },

  updateStatus(id: number, payload: OrderStatusRequest) {
    return apiClient.put<ApiResponse<OrderResponse>>(`/orders/${id}/status`, payload)
  },
}

export default ordersApi
