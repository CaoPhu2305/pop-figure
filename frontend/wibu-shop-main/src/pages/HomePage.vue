<template>
  <div class="home-page">
    <section class="hero-shell section-shell">
      <v-container class="py-12 py-md-16">
        <v-row align="center" class="gy-8">
          <v-col cols="12" lg="7">
            <div class="section-heading mb-4">PopFigure / curated catalog</div>
            <h1 class="text-h3 text-md-h2 font-weight-black page-title mb-4">
              Giao diện mới cho dữ liệu thật từ
              <span class="gradient-text">sản phẩm, danh mục và đơn hàng</span>
            </h1>
            <p class="text-body-1 text-md-h6 muted-copy mb-6" style="max-width: 720px;">
              Mình đã dựng lại dashboard theo đúng backend hiện có: catalog sản phẩm,
              danh mục, hồ sơ tài khoản, lịch sử đơn hàng và khu vực quản trị.
            </p>
            <div class="d-flex flex-wrap ga-3">
              <v-btn color="primary" size="large" rounded="xl" to="/productlist">
                Khám phá catalog
              </v-btn>
              <v-btn variant="outlined" size="large" rounded="xl" to="/order">
                Xem đơn hàng
              </v-btn>
              <v-btn v-if="authStore.isAdmin" variant="tonal" color="secondary" size="large" rounded="xl" to="/admin">
                Vào quản trị
              </v-btn>
            </div>
          </v-col>

          <v-col cols="12" lg="5">
            <v-card class="surface-card hero-card pa-6">
              <div class="d-flex align-center justify-space-between mb-5">
                <div>
                  <div class="text-overline section-heading">System snapshot</div>
                  <div class="text-h5 font-weight-bold">Trạng thái nhanh</div>
                </div>
                <v-avatar color="secondary" rounded="lg" size="44">
                  <v-icon color="white">mdi-view-dashboard-outline</v-icon>
                </v-avatar>
              </div>

              <v-row dense>
                <v-col cols="6">
                  <v-card variant="outlined" class="pa-4 metric-card" rounded="xl">
                    <div class="text-caption muted-copy">Sản phẩm</div>
                    <div class="text-h4 font-weight-black">{{ productCount }}</div>
                  </v-card>
                </v-col>
                <v-col cols="6">
                  <v-card variant="outlined" class="pa-4 metric-card" rounded="xl">
                    <div class="text-caption muted-copy">Danh mục</div>
                    <div class="text-h4 font-weight-black">{{ categoryCount }}</div>
                  </v-card>
                </v-col>
                <v-col cols="6">
                  <v-card variant="outlined" class="pa-4 metric-card" rounded="xl">
                    <div class="text-caption muted-copy">Đơn hàng</div>
                    <div class="text-h4 font-weight-black">{{ orderHint }}</div>
                  </v-card>
                </v-col>
                <v-col cols="6">
                  <v-card variant="outlined" class="pa-4 metric-card" rounded="xl">
                    <div class="text-caption muted-copy">Tài khoản</div>
                    <div class="text-h4 font-weight-black">{{ authStore.isAuthenticated ? 'On' : 'Off' }}</div>
                  </v-card>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <section class="py-6">
      <v-container>
        <v-row>
          <v-col v-for="item in capabilityCards" :key="item.title" cols="12" md="4">
            <v-card class="surface-card h-100 pa-6 hover-lift">
              <v-avatar :color="item.color" rounded="lg" size="44" class="mb-4">
                <v-icon color="white">{{ item.icon }}</v-icon>
              </v-avatar>
              <h3 class="text-h6 font-weight-bold mb-2">{{ item.title }}</h3>
              <p class="muted-copy mb-0">{{ item.description }}</p>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <section class="py-6">
      <v-container>
        <div class="d-flex align-center justify-space-between mb-4">
          <div>
            <div class="section-heading">Danh mục nổi bật</div>
            <h2 class="text-h4 font-weight-bold">Những nhánh dữ liệu chính</h2>
          </div>
          <v-btn variant="text" to="/productlist">Xem catalog</v-btn>
        </div>

        <v-row>
          <v-col v-for="category in categories" :key="category.id" cols="12" sm="6" lg="3">
            <v-card class="surface-card pa-5 h-100">
              <div class="d-flex align-center justify-space-between mb-3">
                <v-avatar color="primary" rounded="lg" size="40">
                  <v-icon color="white">mdi-tag-outline</v-icon>
                </v-avatar>
                <v-chip size="small" variant="tonal" color="secondary">
                  {{ category.slug }}
                </v-chip>
              </div>
              <h3 class="text-h6 font-weight-bold mb-2">{{ category.name }}</h3>
              <p class="muted-copy mb-0 line-clamp-3">
                Danh mục được lấy trực tiếp từ bảng `category` của backend.
              </p>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <section class="py-6">
      <v-container>
        <div class="d-flex align-center justify-space-between mb-4">
          <div>
            <div class="section-heading">Sản phẩm mới</div>
            <h2 class="text-h4 font-weight-bold">Hiển thị ngắn gọn tại trang chủ</h2>
          </div>
          <v-btn color="primary" rounded="xl" to="/productlist">Xem tất cả sản phẩm</v-btn>
        </div>

        <v-row>
          <v-col v-for="product in featuredProducts" :key="product.id" cols="12" sm="6" lg="3">
            <v-card class="surface-card h-100 product-preview-card">
              <v-img
                :src="getProductImage(product)"
                height="170"
                cover
                class="product-preview-image"
              />
              <v-card-text class="pa-4">
                <div class="d-flex align-start justify-space-between mb-2">
                  <v-chip size="x-small" variant="tonal" color="primary">
                    {{ product.categoryName || 'Chưa phân loại' }}
                  </v-chip>
                  <v-chip v-if="product.isVaulted" size="x-small" color="warning" variant="tonal">Vaulted</v-chip>
                </div>
                <h3 class="text-subtitle-1 font-weight-bold mb-1 line-clamp-2">{{ product.name }}</h3>
                <p class="muted-copy text-body-2 line-clamp-2 mb-3">{{ product.description }}</p>
                <div class="d-flex justify-space-between align-center">
                  <span class="text-caption muted-copy">#{{ product.id }}</span>
                  <v-btn :to="`/product?id=${product.id}`" variant="text" color="primary" size="small">Chi tiết</v-btn>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'

import { useAuthStore } from '@/stores/auth.store'
import { useCategoriesStore } from '@/stores/categories.store'
import { useProductsStore } from '@/stores/products.store'

const authStore = useAuthStore()
const categoriesStore = useCategoriesStore()
const productsStore = useProductsStore()

const categories = computed(() => categoriesStore.categories.slice(0, 8))
const featuredProducts = computed(() => productsStore.products.slice(0, 4))
const productCount = computed(() => productsStore.total || productsStore.productCount)
const categoryCount = computed(() => categoriesStore.categories.length)
const orderHint = computed(() => (authStore.isAuthenticated ? 'My' : '0'))

const capabilityCards = [
  {
    title: 'Catalog rõ ràng',
    description: 'Sản phẩm được hiển thị theo đúng field backend: tên, slug, mô tả, vaulted, danh mục.',
    icon: 'mdi-card-text-outline',
    color: 'primary',
  },
  {
    title: 'Luồng tài khoản',
    description: 'Đăng nhập bằng username, đăng ký qua endpoint /users, và giữ phiên bằng JWT thật.',
    icon: 'mdi-account-lock-outline',
    color: 'secondary',
  },
  {
    title: 'Quản trị nghiệp vụ',
    description: 'Tách rõ phần catalog, đơn hàng, người dùng và quyền truy cập để mở rộng sau này.',
    icon: 'mdi-shield-crown-outline',
    color: 'warning',
  },
]

onMounted(async () => {
  await Promise.all([categoriesStore.fetchCategories(), productsStore.fetchProducts()])
})

const getProductImage = (product: { id: number; slug: string }) => {
  return `https://picsum.photos/seed/popfigure-${product.id}-${product.slug}/640/420`
}
</script>

<style scoped>
.hero-shell {
  padding-top: 8px;
}

.hero-card {
  border-radius: 28px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.45);
}

.product-preview-card {
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-preview-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 18px 34px rgba(44, 33, 28, 0.12);
}
</style>
