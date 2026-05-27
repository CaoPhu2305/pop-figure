import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

import authApi from '@/api/auth.api'
import usersApi from '@/api/users.api'
import type { AuthSession, LoginRequest, RegisterRequest, User } from '@/types'

const TOKEN_KEY = 'popfigure_token'
const USER_KEY = 'popfigure_user'

type StoredUser = User & {
  role_name?: string | null
  full_name?: string
}

function decodeJwtScope(token: string): string[] {
  try {
    const payload = token.split('.')[1]
    if (!payload) return []

    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/')
    const json = atob(normalized.padEnd(Math.ceil(normalized.length / 4) * 4, '='))
    const parsed = JSON.parse(json) as { scope?: string }
    return (parsed.scope ?? '')
      .split(' ')
      .map((item) => item.trim())
      .filter(Boolean)
  } catch {
    return []
  }
}

function deriveUserRole(authorities: string[]): string | null {
  const roleToken = authorities.find((authority) => authority.startsWith('ROLE_'))
  if (!roleToken) return null
  return roleToken.replace('ROLE_', '')
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<StoredUser | null>(null)
  const token = ref<string | null>(null)
  const authorities = ref<string[]>([])
  const isLoading = ref(false)
  const isInitialized = ref(false)

  const isAuthenticated = computed(() => Boolean(token.value))
  const isAdmin = computed(() => authorities.value.includes('ROLE_ADMIN'))
  const displayName = computed(() => user.value?.full_name || user.value?.name || 'Khách')
  const initials = computed(() => {
    const source = displayName.value
    return source
      .split(' ')
      .filter(Boolean)
      .map((part) => part[0])
      .slice(0, 2)
      .join('')
      .toUpperCase() || 'PF'
  })

  const hasAuthority = (authority: string) => authorities.value.includes(authority)
  const hasAnyAuthority = (items: string[]) => items.some((item) => hasAuthority(item))
  const hasRole = (roleName: string) => authorities.value.includes(`ROLE_${roleName.toUpperCase()}`)

  const persistToken = (nextToken: string) => {
    token.value = nextToken
    authorities.value = decodeJwtScope(nextToken)
    localStorage.setItem(TOKEN_KEY, nextToken)
    localStorage.setItem(USER_KEY, JSON.stringify(user.value ?? {}))
  }

  const hydrateUser = async () => {
    try {
      const response = await usersApi.me()
      const payload = response.data?.result
      if (payload && typeof payload === 'object') {
        const nextUser = payload as User
        user.value = {
          ...nextUser,
          full_name: nextUser.full_name || nextUser.name,
          role_name: deriveUserRole(authorities.value),
        }
        localStorage.setItem(USER_KEY, JSON.stringify(user.value))
      }
    } catch {
      // Keep the stored token; the session can still be bootstrapped from it.
    }
  }

  const clearSession = () => {
    user.value = null
    token.value = null
    authorities.value = []
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  const initialize = async () => {
    if (isInitialized.value) return

    const savedToken = localStorage.getItem(TOKEN_KEY)
    const savedUser = localStorage.getItem(USER_KEY)

    if (!savedToken) {
      clearSession()
      isInitialized.value = true
      return
    }

    token.value = savedToken
    authorities.value = decodeJwtScope(savedToken)

    if (savedUser) {
      try {
        const parsed = JSON.parse(savedUser) as StoredUser
        user.value = {
          ...parsed,
          full_name: parsed.full_name || parsed.name,
          role_name: parsed.role_name || deriveUserRole(authorities.value),
        }
      } catch {
        user.value = null
      }
    }

    try {
      const response = await authApi.introspect({ token: savedToken })
      const isValid = Boolean(response.data?.result?.valid)
      if (!isValid) {
        clearSession()
      } else if (!user.value) {
        await hydrateUser()
      }
    } catch {
      clearSession()
    } finally {
      isInitialized.value = true
    }
  }

  const login = async (payload: LoginRequest) => {
    isLoading.value = true
    try {
      const response = await authApi.login(payload)
      const session = response.data?.result
      const nextToken = (session as AuthSession | { token?: string })?.token
      if (!nextToken) {
        throw new Error('Không nhận được token từ máy chủ.')
      }

      persistToken(nextToken)
      await hydrateUser()
      return session
    } finally {
      isLoading.value = false
    }
  }

  const register = async (payload: RegisterRequest) => {
    isLoading.value = true
    try {
      await authApi.register(payload)
      return login({ name: payload.name, password: payload.password })
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    const currentToken = token.value
    clearSession()

    if (!currentToken) return

    try {
      await authApi.logout({ token: currentToken })
    } catch {
      // The local sign-out is the important part for the UI.
    }
  }

  return {
    user,
    token,
    authorities,
    isLoading,
    isInitialized,
    isAuthenticated,
    isAdmin,
    displayName,
    initials,
    hasAuthority,
    hasAnyAuthority,
    hasRole,
    initialize,
    login,
    register,
    logout,
  }
})
