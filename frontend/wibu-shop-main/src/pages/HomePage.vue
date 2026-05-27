<template>
  <div class="home-page">
    <section class="hero-shell section-shell">
      <v-container class="py-12 py-md-16">
        <v-row align="center" class="gy-8">
          <v-col cols="12" lg="7">
            <div class="section-heading mb-4">PopFigure / Ecommerce Storefront</div>
            <h1 class="text-h3 text-md-h2 font-weight-black page-title mb-4">
              Thế giới mô hình anime
              <span class="gradient-text">chính hãng, tuyển chọn mỗi tuần</span>
            </h1>
            <p class="text-body-1 text-md-h6 muted-copy mb-6" style="max-width: 720px;">
              PopFigure là cửa hàng chuyên mô hình, figure và collectible dành cho fan anime.
              Giao diện tập trung trải nghiệm mua sắm: khám phá danh mục, xem sản phẩm và chọn item yêu thích nhanh chóng.
            </p>
            <div class="d-flex flex-wrap ga-3">
              <v-btn color="primary" size="large" rounded="xl" to="/products">
                Mua sắm ngay
              </v-btn>
              <v-btn variant="outlined" size="large" rounded="xl" to="/products?category=1">
                Xem bộ sưu tập nổi bật
              </v-btn>
            </div>
          </v-col>

          <v-col cols="12" lg="5">
            <v-card class="surface-card hero-panel pa-5">
              <v-img
                src="https://picsum.photos/seed/popfigure-hero/960/700"
                height="320"
                cover
                class="hero-image"
              />
              <div class="d-flex flex-wrap ga-2 mt-4">
                <v-chip variant="tonal" color="primary">Chính hãng</v-chip>
                <v-chip variant="tonal" color="secondary">Đóng gói cẩn thận</v-chip>
                <v-chip variant="tonal" color="warning">Giao hàng toàn quốc</v-chip>
              </div>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <section class="py-6">
      <v-container>
        <v-row>
          <v-col v-for="item in sellingPoints" :key="item.title" cols="12" md="4">
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
            <div class="section-heading">Danh mục</div>
            <h2 class="text-h4 font-weight-bold">Khám phá theo bộ sưu tập</h2>
          </div>
          <v-btn variant="text" to="/products">Xem toàn bộ sản phẩm</v-btn>
        </div>

        <v-row>
          <v-col v-for="category in categories" :key="category.id" cols="12" sm="6" lg="3">
            <v-card class="surface-card pa-5 h-100 category-card" :to="{ path: '/products', query: { category: category.id } }">
              <div class="d-flex align-center justify-space-between mb-3">
                <v-avatar color="primary" rounded="lg" size="40">
                  <v-icon color="white">mdi-tag-heart-outline</v-icon>
                </v-avatar>
                <v-chip size="small" variant="tonal" color="secondary">{{ category.slug }}</v-chip>
              </div>
              <h3 class="text-h6 font-weight-bold mb-2">{{ category.name }}</h3>
              <p class="muted-copy mb-0">Chọn nhanh các sản phẩm cùng phong cách để mua sắm thuận tiện hơn.</p>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </section>

    <section class="py-6 pb-12">
      <v-container>
        <div class="d-flex align-center justify-space-between mb-4">
          <div>
            <div class="section-heading">Sản phẩm mới</div>
            <h2 class="text-h4 font-weight-bold">Gợi ý dành cho bạn</h2>
          </div>
          <v-btn color="primary" rounded="xl" to="/products">Mở trang sản phẩm</v-btn>
        </div>

        <v-row>
          <v-col v-for="product in featuredProducts" :key="product.id" cols="12" sm="6" lg="3">
            <v-card class="surface-card h-100 product-preview-card" :to="`/products/${product.id}`">
              <v-img
                :src="getProductImage(product)"
                height="176"
                cover
                class="product-preview-image"
              />
              <v-card-text class="pa-4">
                <div class="d-flex align-start justify-space-between mb-2">
                  <v-chip size="x-small" variant="tonal" color="primary">
                    {{ product.categoryName || 'Chưa phân loại' }}
                  </v-chip>
                  <v-chip v-if="product.isVaulted" size="x-small" color="warning" variant="tonal">Limited</v-chip>
                </div>
                <h3 class="text-subtitle-1 font-weight-bold mb-1 line-clamp-2">{{ product.name }}</h3>
                <p class="muted-copy text-body-2 line-clamp-2 mb-3">{{ product.description }}</p>
                <div class="d-flex justify-space-between align-center">
                  <span class="text-caption muted-copy">Mã #{{ product.id }}</span>
                  <v-btn variant="text" color="primary" size="small">Xem chi tiết</v-btn>
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

import { useCategoriesStore } from '@/stores/categories.store'
import { useProductsStore } from '@/stores/products.store'

const categoriesStore = useCategoriesStore()
const productsStore = useProductsStore()

const categories = computed(() => categoriesStore.categories.slice(0, 8))
const featuredProducts = computed(() => productsStore.products.slice(0, 8))

const sellingPoints = [
  {
    title: 'Sản phẩm tuyển chọn',
    description: 'Danh mục được chọn lọc theo xu hướng anime, manga và game hot nhất.',
    icon: 'mdi-star-circle-outline',
    color: 'primary',
  },
  {
    title: 'Đóng gói an toàn',
    description: 'Hàng được đóng hộp chống sốc, kiểm tra kỹ trước khi xuất kho.',
    icon: 'mdi-package-variant-closed-check',
    color: 'secondary',
  },
  {
    title: 'Mua sắm dễ dàng',
    description: 'Tìm sản phẩm theo danh mục hoặc từ khóa chỉ trong vài thao tác.',
    icon: 'mdi-cart-check',
    color: 'warning',
  },
]

onMounted(async () => {
  await Promise.all([categoriesStore.fetchCategories(), productsStore.fetchProducts()])
})

const getProductImage = (product: { id: number; slug: string }) => {
  return `https://picsum.photos/seed/home-${product.id}-${product.slug}/640/420`
}
</script>

<style scoped>
.hero-shell {
  padding-top: 8px;
}

.hero-panel {
  border-radius: 26px;
}

.hero-image {
  border-radius: 18px;
}

.category-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 34px rgba(44, 33, 28, 0.1);
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
