import apiClient from '@/api/index'
import type { ApiResponse, RoleResponse } from '@/types'

export const rolesApi = {
  getAll() {
    return apiClient.get<ApiResponse<RoleResponse[]>>('/roles')
  },
}

export default rolesApi
