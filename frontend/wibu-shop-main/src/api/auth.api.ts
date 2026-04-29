import apiClient from '@/api/index';
import type { AuthResponse, LoginRequest, RegisterRequest } from '@/types';

export const authApi = {
  login(payload: LoginRequest) {
    return apiClient.post<AuthResponse>('/auth/login', payload);
  },

  register(payload: RegisterRequest) {
    return apiClient.post<AuthResponse>('/auth/register', payload);
  },

  me() {
    return apiClient.get('/auth/me');
  },
};

export default authApi;
