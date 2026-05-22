import apiClient from '@/api/index'
import type { ApiResponse, User } from '@/types'

export const usersApi = {
  me() {
    return apiClient.get<ApiResponse<User>>('/users/myInfo')
  },

  list() {
    return apiClient.get<ApiResponse<User[]>>('/users')
  },
}

export default usersApi
