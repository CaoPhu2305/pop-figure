<template>
  <v-container class="py-10">
    <v-card class="surface-card pa-6 mb-6">
      <div class="d-flex flex-column flex-md-row justify-space-between ga-4">
        <div>
          <div class="section-heading mb-2">Orders</div>
          <h1 class="text-h4 font-weight-black mb-2">Đơn hàng của tôi</h1>
          <p class="muted-copy mb-0">Trang này bám đúng backend: `orders/my` cho người dùng và `orders` cho quản trị.</p>
        </div>
        <div class="d-flex flex-wrap ga-3">
          <v-btn variant="outlined" :loading="loading" @click="loadOrders">Làm mới</v-btn>
          <v-btn v-if="authStore.isAdmin" color="primary" variant="tonal" @click="showAdminView = !showAdminView">
            {{ showAdminView ? 'Ẩn view quản trị' : 'Xem quản trị' }}
          </v-btn>
        </div>
      </div>
    </v-card>

    <v-row class="gy-6">
      <v-col cols="12" lg="8">
        <v-card class="surface-card pa-4">
          <v-tabs v-model="activeTab" color="primary" class="mb-4">
            <v-tab value="mine">Đơn của tôi</v-tab>
            <v-tab v-if="authStore.isAdmin && showAdminView" value="all">Tất cả đơn</v-tab>
          </v-tabs>

          <v-window v-model="activeTab">
            <v-window-item value="mine">
              <div v-if="loading" class="pa-6">
                <v-skeleton-loader v-for="n in 3" :key="n" type="article" class="mb-3" />
              </div>
              <div v-else-if="myOrders.length === 0" class="text-center py-10">
                <v-icon size="64" color="primary" class="mb-3">mdi-receipt-text-off-outline</v-icon>
                <h2 class="text-h6 font-weight-bold mb-2">Chưa có đơn hàng</h2>
                <p class="muted-copy mb-0">Khi backend có luồng tạo đơn, các đơn này sẽ xuất hiện ở đây.</p>
              </div>
              <v-list v-else>
                <v-list-item v-for="order in myOrders" :key="order.id" class="mb-2" rounded="lg" variant="outlined">
                  <template #prepend>
                    <v-avatar color="primary" rounded="lg">
                      <v-icon color="white">mdi-package-variant</v-icon>
                    </v-avatar>
                  </template>
                  <v-list-item-title class="font-weight-bold">#{{ order.id }}</v-list-item-title>
                  <v-list-item-subtitle>
                    {{ order.username || 'N/A' }} • {{ formatDate(order.createdAt || order.created_at) }}
                  </v-list-item-subtitle>
                  <template #append>
                    <div class="text-right">
                      <v-chip size="small" :color="statusColor(order.status)" variant="tonal" class="mb-1">
                        {{ order.status }}
                      </v-chip>
                      <div class="font-weight-bold">{{ formatMoney(order.totalAmount || order.total_amount || 0) }}</div>
                    </div>
                  </template>
                </v-list-item>
              </v-list>
            </v-window-item>

            <v-window-item v-if="authStore.isAdmin && showAdminView" value="all">
              <div class="text-subtitle-2 muted-copy mb-3">Có thể đổi trạng thái trực tiếp từ API `/orders/{id}/status`.</div>
              <v-list>
                <v-list-item v-for="order in allOrders" :key="order.id" class="mb-2" rounded="lg" variant="outlined">
                  <v-list-item-title class="font-weight-bold">#{{ order.id }} - {{ order.username || 'N/A' }}</v-list-item-title>
                  <v-list-item-subtitle>{{ formatDate(order.createdAt || order.created_at) }}</v-list-item-subtitle>
                  <template #append>
                    <div class="d-flex align-center ga-2">
                      <v-select
                        v-model="statusDraft[order.id]"
                        :items="statusOptions"
                        item-title="title"
                        item-value="value"
                        density="compact"
                        hide-details
                        variant="outlined"
                        style="width: 160px;"
                      />
                      <v-btn size="small" color="primary" @click="updateStatus(order.id)">Lưu</v-btn>
                    </div>
                  </template>
                </v-list-item>
              </v-list>
            </v-window-item>
          </v-window>
        </v-card>
      </v-col>

      <v-col cols="12" lg="4">
        <v-card class="surface-card pa-6 mb-6">
          <div class="section-heading mb-2">Tóm tắt</div>
          <h2 class="text-h6 font-weight-bold mb-4">Order metrics</h2>
          <div class="d-flex flex-column ga-3">
            <div class="d-flex justify-space-between">
              <span class="muted-copy">Đơn của tôi</span>
              <strong>{{ myOrders.length }}</strong>
            </div>
            <div class="d-flex justify-space-between">
              <span class="muted-copy">Tất cả đơn</span>
              <strong>{{ allOrders.length }}</strong>
            </div>
            <div class="d-flex justify-space-between">
              <span class="muted-copy">Quyền quản trị</span>
              <strong>{{ authStore.isAdmin ? 'Yes' : 'No' }}</strong>
            </div>
          </div>
        </v-card>

        <v-card class="surface-card pa-6">
          <div class="section-heading mb-2">Ghi chú</div>
          <h2 class="text-h6 font-weight-bold mb-3">Luồng nghiệp vụ</h2>
          <p class="muted-copy mb-0">
            Backend hiện có phần xem và cập nhật trạng thái đơn, nên UI này ưu tiên lịch sử đơn và thao tác admin.
            Nếu sau này thêm create-order API, mình sẽ nối checkout vào ngay.
          </p>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'

import ordersApi from '@/api/orders.api'
import { useAuthStore } from '@/stores/auth.store'
import type { OrderResponse } from '@/types'

const authStore = useAuthStore()
const loading = ref(false)
const myOrders = ref<OrderResponse[]>([])
const allOrders = ref<OrderResponse[]>([])
const showAdminView = ref(false)
const activeTab = ref<'mine' | 'all'>('mine')
const statusDraft = reactive<Record<number, string>>({})

const statusOptions = [
  { title: 'pending', value: 'pending' },
  { title: 'processing', value: 'processing' },
  { title: 'shipped', value: 'shipped' },
  { title: 'delivered', value: 'delivered' },
  { title: 'cancelled', value: 'cancelled' },
]

const statusColor = (status?: string) => {
  switch (status) {
    case 'delivered':
      return 'success'
    case 'shipped':
      return 'info'
    case 'processing':
      return 'warning'
    case 'cancelled':
      return 'error'
    default:
      return 'secondary'
  }
}

const formatMoney = (value: number) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
const formatDate = (value?: string) => (value ? new Date(value).toLocaleString('vi-VN') : 'N/A')

const loadOrders = async () => {
  loading.value = true
  try {
    const mineResponse = await ordersApi.getMyOrders()
    myOrders.value = (mineResponse.data?.result ?? []) as OrderResponse[]

    if (authStore.isAdmin && showAdminView.value) {
      const allResponse = await ordersApi.getAllOrders()
      allOrders.value = (allResponse.data?.result ?? []) as OrderResponse[]
      allOrders.value.forEach((order) => {
        statusDraft[order.id] = order.status
      })
    }
  } finally {
    loading.value = false
  }
}

const updateStatus = async (orderId: number) => {
  const status = statusDraft[orderId]
  if (!status) return
  await ordersApi.updateStatus(orderId, { status })
  await loadOrders()
}

onMounted(async () => {
  await loadOrders()
})
</script>
