<template>
  <v-container class="py-8">
    <v-card class="surface-card pa-6 mb-6">
      <div class="d-flex flex-column flex-md-row justify-space-between ga-4">
        <div>
          <div class="section-heading mb-2">Operations dashboard</div>
          <h1 class="text-h4 font-weight-black mb-2">Quản trị vận hành cửa hàng</h1>
          <p class="muted-copy mb-0">Theo dõi catalog và đơn hàng. Quản lý tài khoản đã được tách riêng sang trang Accounts.</p>
        </div>
        <v-btn color="primary" variant="tonal" :loading="loading" @click="loadAll">Làm mới</v-btn>
      </div>
    </v-card>

    <v-row class="gy-6 mb-6">
      <v-col v-for="metric in metrics" :key="metric.label" cols="12" sm="6" lg="3">
        <v-card class="surface-card pa-5 h-100">
          <div class="text-caption muted-copy">{{ metric.label }}</div>
          <div class="text-h4 font-weight-black">{{ metric.value }}</div>
          <div class="text-caption muted-copy">{{ metric.hint }}</div>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="gy-6">
      <v-col cols="12" lg="6">
        <v-card class="surface-card pa-5 h-100">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Danh mục</h2>
            <v-chip size="small" color="primary" variant="tonal">{{ categories.length }}</v-chip>
          </div>
          <v-list density="compact">
            <v-list-item v-for="category in categories" :key="category.id" :title="category.name" :subtitle="category.slug" />
          </v-list>
        </v-card>
      </v-col>

      <v-col cols="12" lg="6">
        <v-card class="surface-card pa-5 h-100">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Sản phẩm gần nhất</h2>
            <v-chip size="small" color="secondary" variant="tonal">{{ products.length }}</v-chip>
          </div>
          <v-list density="compact">
            <v-list-item
              v-for="product in products.slice(0, 8)"
              :key="product.id"
              :title="product.name"
              :subtitle="`${product.slug} • ${product.categoryName || 'N/A'}`"
            />
          </v-list>
        </v-card>
      </v-col>

      <v-col cols="12">
        <v-card class="surface-card pa-5">
          <div class="d-flex justify-space-between align-center mb-4">
            <h2 class="text-h6 font-weight-bold">Đơn hàng</h2>
            <v-chip size="small" color="warning" variant="tonal">{{ orders.length }}</v-chip>
          </div>
          <v-table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Khách</th>
                <th>Ngày tạo</th>
                <th>Trạng thái</th>
                <th class="text-right">Tổng</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders.slice(0, 10)" :key="order.id">
                <td>#{{ order.id }}</td>
                <td>{{ order.username || order.userId || 'N/A' }}</td>
                <td>{{ formatDate(order.createdAt || order.created_at) }}</td>
                <td><v-chip size="small" :color="statusColor(order.status)" variant="tonal">{{ order.status }}</v-chip></td>
                <td class="text-right">{{ formatMoney(order.totalAmount || order.total_amount || 0) }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'

import categoriesApi from '@/api/categories.api'
import ordersApi from '@/api/orders.api'
import productsApi from '@/api/products.api'
import type { CategoryResponse, OrderResponse, ProductResponse } from '@/types'

const loading = ref(false)
const categories = ref<CategoryResponse[]>([])
const products = ref<ProductResponse[]>([])
const orders = ref<OrderResponse[]>([])

const metrics = computed(() => [
  { label: 'Categories', value: categories.value.length, hint: 'Danh mục hiện có' },
  { label: 'Products', value: products.value.length, hint: 'Sản phẩm trong catalog' },
  { label: 'Orders', value: orders.value.length, hint: 'Tổng đơn hàng' },
  { label: 'Order status', value: orders.value.filter((o) => (o.status || '').toLowerCase() === 'processing').length, hint: 'Đơn đang xử lý' },
])

const statusColor = (status?: string) => {
  switch ((status || '').toLowerCase()) {
    case 'delivered': return 'success'
    case 'shipped': return 'info'
    case 'processing': return 'warning'
    case 'cancelled': return 'error'
    default: return 'secondary'
  }
}

const formatMoney = (value: number) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
const formatDate = (value?: string) => (value ? new Date(value).toLocaleString('vi-VN') : 'N/A')

const loadAll = async () => {
  loading.value = true
  try {
    const [catRes, prodRes, orderRes] = await Promise.all([
      categoriesApi.getAll(),
      productsApi.getAll(),
      ordersApi.getAllOrders(),
    ])
    categories.value = (catRes.data?.result ?? []) as CategoryResponse[]
    products.value = (prodRes.data?.result ?? []) as ProductResponse[]
    orders.value = (orderRes.data?.result ?? []) as OrderResponse[]
  } finally {
    loading.value = false
  }
}

onMounted(loadAll)
</script>
