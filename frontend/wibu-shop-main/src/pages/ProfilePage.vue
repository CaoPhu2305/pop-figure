<template>
  <v-container class="py-10">
    <v-row class="gy-6">
      <v-col cols="12" lg="4">
        <v-card class="surface-card pa-6 sticky-top">
          <div class="text-center">
            <v-avatar color="primary" size="88" rounded="xl" class="mb-4">
              <span class="text-h5 font-weight-black text-white">{{ authStore.initials }}</span>
            </v-avatar>
            <h1 class="text-h5 font-weight-black mb-1">{{ authStore.displayName }}</h1>
            <p class="muted-copy mb-4">{{ profile?.email || profile?.name || 'Chưa tải thông tin' }}</p>
            <v-chip color="primary" variant="tonal">{{ profile?.role_name || 'CUSTOMER' }}</v-chip>
          </div>

          <v-divider class="my-6" />

          <div class="d-flex flex-column ga-3">
            <div>
              <div class="text-caption muted-copy">Tài khoản</div>
              <div class="font-weight-medium">{{ profile?.name || 'N/A' }}</div>
            </div>
            <div>
              <div class="text-caption muted-copy">Email</div>
              <div class="font-weight-medium">{{ profile?.email || 'N/A' }}</div>
            </div>
            <div>
              <div class="text-caption muted-copy">Điện thoại</div>
              <div class="font-weight-medium">{{ profile?.phone || profile?.phone_number || 'N/A' }}</div>
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" lg="8">
        <v-card class="surface-card pa-6 mb-6">
          <div class="d-flex align-center justify-space-between mb-4">
            <div>
              <div class="section-heading mb-1">Thông tin</div>
              <h2 class="text-h5 font-weight-bold">Hồ sơ người dùng</h2>
            </div>
            <v-btn variant="outlined" :loading="loading" @click="reloadProfile">Làm mới</v-btn>
          </div>

          <v-row dense>
            <v-col cols="12" md="6">
              <v-card variant="outlined" class="pa-4">
                <div class="text-caption muted-copy">User ID</div>
                <div class="text-h6 font-weight-bold">{{ profile?.id ?? 'N/A' }}</div>
              </v-card>
            </v-col>
            <v-col cols="12" md="6">
              <v-card variant="outlined" class="pa-4">
                <div class="text-caption muted-copy">Quyền đã decode</div>
                <div class="text-h6 font-weight-bold">{{ authStore.authorities.length }}</div>
              </v-card>
            </v-col>
          </v-row>
        </v-card>

        <v-card class="surface-card pa-6">
          <div class="section-heading mb-2">Authorities</div>
          <h2 class="text-h5 font-weight-bold mb-4">Scope hiện có từ JWT</h2>
          <div class="d-flex flex-wrap ga-2">
            <v-chip v-for="scope in authStore.authorities" :key="scope" color="secondary" variant="tonal">
              {{ scope }}
            </v-chip>
            <div v-if="authStore.authorities.length === 0" class="muted-copy">
              Chưa có authority nào được decode từ token.
            </div>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

import usersApi from '@/api/users.api'
import { useAuthStore } from '@/stores/auth.store'
import type { User } from '@/types'

const authStore = useAuthStore()
const profile = ref<User | null>(null)
const loading = ref(false)

const reloadProfile = async () => {
  loading.value = true
  try {
    const response = await usersApi.me()
    profile.value = response.data?.result ?? null
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await reloadProfile()
})
</script>
