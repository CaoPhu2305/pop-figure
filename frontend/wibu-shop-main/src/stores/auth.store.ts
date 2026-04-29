import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

import authApi from '@/api/auth.api';
import type { AuthResponse, LoginRequest, RegisterRequest, User } from '@/types';

const TOKEN_KEY = 'popfigure_token';
const USER_KEY = 'popfigure_user';

type AuthUser = User & {
  role_name?: string | null;
};

export const useAuthStore = defineStore('auth', () => {
  const user = ref<AuthUser | null>(null);
  const token = ref<string | null>(null);
  const isLoading = ref(false);
  const isInitialized = ref(false);

  const isAuthenticated = computed(() => Boolean(token.value && user.value));
  const isAdmin = computed(() => user.value?.role_name?.toUpperCase() === 'ADMIN');

  const displayName = computed(() => user.value?.full_name || user.value?.name || 'Guest');

  const initials = computed(() => {
    const source = user.value?.full_name || user.value?.name;
    if (!source) return 'G';

    return source
      .split(' ')
      .filter(Boolean)
      .map((part) => part[0])
      .slice(0, 2)
      .join('')
      .toUpperCase();
  });

  const persistSession = (session: AuthResponse) => {
    user.value = session.user as AuthUser;
    token.value = session.token;
    localStorage.setItem(USER_KEY, JSON.stringify(session.user));
    localStorage.setItem(TOKEN_KEY, session.token);
    isInitialized.value = true;
  };

  const clearStoredSession = () => {
    user.value = null;
    token.value = null;
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(TOKEN_KEY);
  };

  const initialize = () => {
    if (isInitialized.value) {
      return;
    }

    const savedToken = localStorage.getItem(TOKEN_KEY);
    const savedUser = localStorage.getItem(USER_KEY);

    if (savedToken && savedUser) {
      try {
        token.value = savedToken;
        user.value = JSON.parse(savedUser) as AuthUser;
      } catch {
        clearStoredSession();
      }
    } else {
      clearStoredSession();
    }

    isInitialized.value = true;
  };

  const login = async (payload: LoginRequest) => {
    isLoading.value = true;
    try {
      const { data } = await authApi.login(payload);
      persistSession(data);
      return data;
    } finally {
      isLoading.value = false;
    }
  };

  const register = async (payload: RegisterRequest) => {
    isLoading.value = true;
    try {
      const { data } = await authApi.register(payload);
      persistSession(data);
      return data;
    } finally {
      isLoading.value = false;
    }
  };

  const logout = () => {
    clearStoredSession();
  };

  const refreshProfile = async () => {
    if (!token.value) {
      return null;
    }

    const { data } = await authApi.me();
    user.value = data as AuthUser;
    localStorage.setItem(USER_KEY, JSON.stringify(data));
    return data;
  };

  return {
    user,
    token,
    isLoading,
    isInitialized,
    isAuthenticated,
    isAdmin,
    displayName,
    initials,
    initialize,
    login,
    register,
    logout,
    refreshProfile,
  };
});
