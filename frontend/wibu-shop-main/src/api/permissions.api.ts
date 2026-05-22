import apiClient from '@/api/index'
import type { ApiResponse, PermissionResponse } from '@/types'

export const permissionsApi = {
  getAll() {
    return apiClient.get<ApiResponse<PermissionResponse[]>>('/permissions')
  },
}

export default permissionsApi
