<template>
  <v-main class="login-shell">
    <v-container class="fill-height py-8" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="10" md="8" lg="6" xl="5">
          <v-card class="surface-card overflow-hidden login-frame">
            <div class="hero-pane pa-8 pa-lg-10 text-center">
              <v-avatar color="primary" size="62" rounded="xl" class="mb-4">
                <v-icon color="white" size="30">mdi-account-circle-outline</v-icon>
              </v-avatar>
              <h1 class="text-h4 font-weight-black mb-2">Đăng nhập tài khoản</h1>
              <p class="muted-copy mb-0">Đăng nhập để quản lý đơn hàng và tiếp tục mua sắm.</p>
            </div>

            <div class="form-pane pa-8 pa-lg-10">
              <v-form @submit.prevent="handleLogin">
                <v-text-field
                  v-model="form.name"
                  label="Tên đăng nhập hoặc email"
                  variant="outlined"
                  rounded="xl"
                  class="mb-4"
                  prepend-inner-icon="mdi-account-outline"
                  autocomplete="username"
                />

                <v-text-field
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  label="Mật khẩu"
                  variant="outlined"
                  rounded="xl"
                  class="mb-3"
                  prepend-inner-icon="mdi-lock-outline"
                  :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  autocomplete="current-password"
                  @click:append-inner="showPassword = !showPassword"
                />

                <div class="d-flex justify-space-between mb-4 text-caption">
                  <span class="muted-copy">Chưa có tài khoản?</span>
                  <router-link to="/register" class="text-decoration-none">Đăng ký ngay</router-link>
                </div>

                <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4">
                  {{ errorMessage }}
                </v-alert>

                <v-btn type="submit" block size="large" color="primary" :loading="authStore.isLoading" rounded="xl">
                  Đăng nhập
                </v-btn>
              </v-form>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { useAuthStore } from '@/stores/auth.store'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const form = reactive({ name: '', password: '' })
const showPassword = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  errorMessage.value = ''
  if (!form.name.trim() || !form.password.trim()) {
    errorMessage.value = 'Vui lòng nhập đầy đủ tài khoản và mật khẩu.'
    return
  }

  try {
    await authStore.login({ name: form.name.trim(), password: form.password })
    const redirect = String(route.query.redirect ?? '/')
    router.replace(redirect)
  } catch (error) {
    errorMessage.value = (error as Error).message || 'Đăng nhập thất bại.'
  }
}
</script>

<style scoped>
.login-shell {
  background:
    radial-gradient(circle at top right, rgba(196, 79, 45, 0.1), transparent 32%),
    radial-gradient(circle at bottom left, rgba(53, 109, 107, 0.1), transparent 30%);
}

.login-frame { border-radius: 24px; }

.hero-pane {
  background: linear-gradient(165deg, rgba(196, 79, 45, 0.1), rgba(53, 109, 107, 0.08));
}

.form-pane { background: rgba(255, 252, 246, 0.92); }
</style>
