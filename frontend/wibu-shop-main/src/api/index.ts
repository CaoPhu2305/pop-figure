import axios, { AxiosError } from 'axios'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  withCredentials: true,
})

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('popfigure_token')
  if (token) {
    config.headers = config.headers ?? {}
    ;(config.headers as Record<string, string>).Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => Promise.reject(error),
)

export function extractResult<T>(payload: unknown): T | null {
  if (!payload || typeof payload !== 'object') return null

  if ('result' in payload) {
    return (payload as { result?: T }).result ?? null
  }

  if ('data' in payload) {
    return (payload as { data?: T }).data ?? null
  }

  return payload as T
}

export default apiClient
