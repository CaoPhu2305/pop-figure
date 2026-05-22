<template>
  <v-main>
    <v-container class="fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="10" md="8" lg="6" xl="5">
          <v-card class="surface-card pa-8 register-card">
            <div class="text-center mb-8">
              <v-avatar color="secondary" size="72" rounded="xl" class="mb-4">
                <v-icon color="white" size="34">mdi-account-plus-outline</v-icon>
              </v-avatar>
              <h1 class="text-h4 font-weight-black mb-2">Tạo tài khoản</h1>
              <p class="muted-copy mb-0">Biểu mẫu này gửi đúng field mà backend đang nhận.</p>
            </div>

            <v-form @submit.prevent="handleRegister">
              <v-row>
                <v-col cols="12" md="6">
                  <v-text-field v-model="form.name" label="Tên đăng nhập" variant="outlined" rounded="xl" />
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="form.email" label="Email" variant="outlined" rounded="xl" />
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="form.phone" label="Số điện thoại" variant="outlined" rounded="xl" />
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field
                    v-model="form.password"
                    :type="showPassword ? 'text' : 'password'"
                    label="Mật khẩu"
                    variant="outlined"
                    rounded="xl"
                    :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    @click:append-inner="showPassword = !showPassword"
                  />
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="form.password_confirmation"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    label="Xác nhận mật khẩu"
                    variant="outlined"
                    rounded="xl"
                    :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    @click:append-inner="showConfirmPassword = !showConfirmPassword"
                  />
                </v-col>
              </v-row>

              <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4">
                {{ errorMessage }}
              </v-alert>

              <v-alert v-if="successMessage" type="success" variant="tonal" class="mb-4">
                {{ successMessage }}
              </v-alert>

              <v-btn
                type="submit"
                block
                size="large"
                color="secondary"
                :loading="authStore.isLoading"
                rounded="xl"
                class="mb-4"
              >
                Đăng ký
              </v-btn>

              <div class="text-center">
                <span class="muted-copy">Đã có tài khoản? </span>
                <router-link to="/login" class="font-weight-bold">Đăng nhập</router-link>
              </div>
            </v-form>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import { useAuthStore } from '@/stores/auth.store'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  name: '',
  email: '',
  phone: '',
  password: '',
  password_confirmation: '',
})

const showPassword = ref(false)
const showConfirmPassword = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const handleRegister = async () => {
  errorMessage.value = ''
  successMessage.value = ''

  if (form.password !== form.password_confirmation) {
    errorMessage.value = 'Mật khẩu xác nhận không khớp.'
    return
  }

  try {
    await authStore.register({
      name: form.name.trim(),
      email: form.email.trim(),
      phone: form.phone.trim(),
      password: form.password,
      password_confirmation: form.password_confirmation,
    })

    successMessage.value = 'Tài khoản đã được tạo. Đang chuyển hướng...'
    setTimeout(() => {
      router.replace('/')
    }, 800)
  } catch (error) {
    errorMessage.value = (error as Error).message || 'Đăng ký thất bại.'
  }
}
</script>
