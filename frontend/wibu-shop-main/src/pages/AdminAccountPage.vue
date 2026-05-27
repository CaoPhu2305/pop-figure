<template>
  <v-container class="py-8">
    <v-card class="surface-card pa-6 mb-6">
      <div class="d-flex flex-column flex-md-row justify-space-between ga-4">
        <div>
          <div class="section-heading mb-2">Account management</div>
          <h1 class="text-h4 font-weight-black mb-2">Quản lý tài khoản hệ thống</h1>
          <p class="muted-copy mb-0">Khu vực dành riêng cho admin theo dõi người dùng, vai trò và quyền.</p>
        </div>
        <v-btn color="primary" variant="tonal" :loading="loading" @click="loadAll">Làm mới</v-btn>
      </div>
    </v-card>

    <v-row class="gy-6">
      <v-col cols="12" lg="6">
        <v-card class="surface-card pa-5 h-100">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Tài khoản người dùng</h2>
            <v-chip size="small" color="primary" variant="tonal">{{ users.length }}</v-chip>
          </div>

          <v-table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Email/SĐT</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>#{{ user.id }}</td>
                <td>{{ user.name }}</td>
                <td>{{ user.email || user.phone || 'N/A' }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>

      <v-col cols="12" lg="6">
        <v-card class="surface-card pa-5 h-100 mb-6">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Vai trò</h2>
            <v-chip size="small" color="secondary" variant="tonal">{{ roles.length }}</v-chip>
          </div>
          <v-expansion-panels variant="accordion">
            <v-expansion-panel v-for="role in roles" :key="role.name">
              <v-expansion-panel-title>{{ role.name }}</v-expansion-panel-title>
              <v-expansion-panel-text>
                <p class="muted-copy">{{ role.description || 'Không có mô tả' }}</p>
                <div class="d-flex flex-wrap ga-2">
                  <v-chip v-for="permission in role.permissions || []" :key="permission.name" size="small" color="primary" variant="tonal">
                    {{ permission.name }}
                  </v-chip>
                </div>
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </v-card>

        <v-card class="surface-card pa-5 h-100">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Danh sách quyền</h2>
            <v-chip size="small" color="warning" variant="tonal">{{ permissions.length }}</v-chip>
          </div>
          <div class="d-flex flex-wrap ga-2">
            <v-chip v-for="permission in permissions" :key="permission.name" size="small" variant="tonal">
              {{ permission.name }}
            </v-chip>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

import permissionsApi from '@/api/permissions.api'
import rolesApi from '@/api/roles.api'
import usersApi from '@/api/users.api'
import type { PermissionResponse, RoleResponse, User } from '@/types'

const loading = ref(false)
const users = ref<User[]>([])
const roles = ref<RoleResponse[]>([])
const permissions = ref<PermissionResponse[]>([])

const loadAll = async () => {
  loading.value = true
  try {
    const [userRes, roleRes, permissionRes] = await Promise.all([
      usersApi.list(),
      rolesApi.getAll(),
      permissionsApi.getAll(),
    ])

    users.value = (userRes.data?.result ?? []) as User[]
    roles.value = (roleRes.data?.result ?? []) as RoleResponse[]
    permissions.value = (permissionRes.data?.result ?? []) as PermissionResponse[]
  } finally {
    loading.value = false
  }
}

onMounted(loadAll)
</script>
