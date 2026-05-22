<template>
  <v-main class="login-shell">
    <v-container class="fill-height py-8" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" md="11" lg="10" xl="9">
          <v-card class="surface-card overflow-hidden login-frame">
            <v-row no-gutters>
              <v-col cols="12" lg="5" class="hero-pane pa-8 pa-lg-10">
                <div class="text-overline section-heading mb-3">PopFigure access</div>
                <h1 class="text-h4 font-weight-black mb-4">Dang nhap vao he thong</h1>
                <p class="muted-copy mb-6">
                  Dung ten dang nhap hoac email, sau do nhap mat khau de tiep tuc.
                </p>
                <div class="d-flex flex-column ga-3">
                  <v-card variant="outlined" class="pa-3 mini-hint">
                    <div class="text-caption muted-copy">Tai khoan mau</div>
                    <div class="font-weight-bold">admin / admin@popfigure.local</div>
                  </v-card>
                  <v-card variant="outlined" class="pa-3 mini-hint">
                    <div class="text-caption muted-copy">Mat khau mau</div>
                    <div class="font-weight-bold">Admin123!</div>
                  </v-card>
                </div>
              </v-col>

              <v-col cols="12" lg="7" class="form-pane pa-8 pa-lg-10">
                <div class="d-flex align-center justify-space-between mb-6">
                  <div>
                    <h2 class="text-h5 font-weight-bold mb-1">Welcome back</h2>
                    <p class="muted-copy mb-0">Nhap thong tin tai khoan de tiep tuc.</p>
                  </div>
                  <v-avatar color="primary" size="44" rounded="xl">
                    <v-icon color="white">mdi-lock-check-outline</v-icon>
                  </v-avatar>
                </div>

                <v-form @submit.prevent="handleLogin">
                  <v-text-field
                    v-model="form.name"
                    label="Ten dang nhap hoac email"
                    variant="outlined"
                    rounded="xl"
                    class="mb-4"
                    prepend-inner-icon="mdi-account-outline"
                    autocomplete="username"
                  />

                  <v-text-field
                    v-model="form.password"
                    :type="showPassword ? 'text' : 'password'"
                    label="Mat khau"
                    variant="outlined"
                    rounded="xl"
                    class="mb-2"
                    prepend-inner-icon="mdi-lock-outline"
                    :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    autocomplete="current-password"
                    @click:append-inner="showPassword = !showPassword"
                  />

                  <div class="d-flex justify-end mb-4">
                    <router-link to="/register" class="text-caption text-decoration-none">
                      Chua co tai khoan?
                    </router-link>
                  </div>

                  <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4">
                    {{ errorMessage }}
                  </v-alert>

                  <v-btn
                    type="submit"
                    block
                    size="large"
                    color="primary"
                    :loading="authStore.isLoading"
                    rounded="xl"
                    class="mb-4"
                  >
                    Dang nhap
                  </v-btn>

                  <div class="text-center muted-copy text-caption">
                    Dang nhap thanh cong se duoc chuyen huong ve trang ban dang mo.
                  </div>
                </v-form>
              </v-col>
            </v-row>
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

const form = reactive({
  name: '',
  password: '',
})

const showPassword = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  errorMessage.value = ''

  if (!form.name.trim() || !form.password.trim()) {
    errorMessage.value = 'Vui long nhap day du tai khoan va mat khau.'
    return
  }

  try {
    await authStore.login({
      name: form.name.trim(),
      password: form.password,
    })

    const redirect = String(route.query.redirect ?? '/')
    router.replace(redirect)
  } catch (error) {
    errorMessage.value = (error as Error).message || 'Dang nhap that bai.'
  }
}
</script>

<style scoped>
.login-shell {
  background:
    radial-gradient(circle at top right, rgba(196, 79, 45, 0.12), transparent 36%),
    radial-gradient(circle at bottom left, rgba(53, 109, 107, 0.12), transparent 32%);
}

.login-frame {
  border-radius: 28px;
}

.hero-pane {
  background:
    linear-gradient(160deg, rgba(196, 79, 45, 0.12), rgba(53, 109, 107, 0.08)),
    rgba(255, 250, 243, 0.6);
}

.form-pane {
  background: rgba(255, 250, 243, 0.92);
}

.mini-hint {
  background: rgba(255, 255, 255, 0.4);
}
</style>
