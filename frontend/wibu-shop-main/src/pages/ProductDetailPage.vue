<template>
  <div class="product-detail-page">
    <v-container class="py-10">
      <v-alert v-if="!productId" type="info" variant="tonal" class="mb-6">
        Thiếu `id` của sản phẩm. Hãy quay lại danh sách và chọn một sản phẩm cụ thể.
      </v-alert>

      <v-skeleton-loader v-if="productsStore.isLoading && !currentProduct" class="surface-card pa-8" type="article, image, actions" />

      <template v-else-if="currentProduct">
        <v-row class="gy-6">
          <v-col cols="12" lg="7">
            <v-card class="surface-card pa-6 h-100">
              <div class="d-flex flex-wrap ga-2 mb-4">
                <v-chip color="primary" variant="tonal">{{ currentProduct.categoryName || 'Chưa phân loại' }}</v-chip>
                <v-chip v-if="currentProduct.isVaulted" color="warning" variant="tonal">Vaulted</v-chip>
                <v-chip variant="outlined">{{ currentProduct.slug }}</v-chip>
              </div>

              <h1 class="text-h4 text-md-h3 font-weight-black mb-3">{{ currentProduct.name }}</h1>
              <p class="text-body-1 muted-copy mb-6">{{ currentProduct.description }}</p>

              <v-row dense>
                <v-col cols="12" md="6">
                  <v-card variant="outlined" class="pa-4">
                    <div class="text-caption muted-copy mb-1">ID</div>
                    <div class="text-h6 font-weight-bold">{{ currentProduct.id }}</div>
                  </v-card>
                </v-col>
                <v-col cols="12" md="6">
                  <v-card variant="outlined" class="pa-4">
                    <div class="text-caption muted-copy mb-1">Danh mục</div>
                    <div class="text-h6 font-weight-bold">{{ currentProduct.categoryName || 'N/A' }}</div>
                  </v-card>
                </v-col>
              </v-row>

              <div class="d-flex flex-wrap ga-3 mt-6">
                <v-btn color="primary" to="/productlist">Quay lại catalog</v-btn>
                <v-btn variant="outlined" @click="copySlug">Sao chép slug</v-btn>
              </div>
            </v-card>
          </v-col>

          <v-col cols="12" lg="5">
            <v-card class="surface-card pa-6 mb-6">
              <div class="section-heading mb-2">Related</div>
              <h2 class="text-h5 font-weight-bold mb-4">Sản phẩm cùng danh mục</h2>
              <v-list class="pa-0" lines="two">
                <template v-for="item in relatedProducts" :key="item.id">
                  <v-divider />
                  <v-list-item :to="`/product?id=${item.id}`" :title="item.name" :subtitle="item.slug">
                    <template #append>
                      <v-chip size="small" variant="tonal">{{ item.id }}</v-chip>
                    </template>
                  </v-list-item>
                </template>
              </v-list>
              <div v-if="relatedProducts.length === 0" class="text-center py-6 muted-copy">
                Chưa có sản phẩm liên quan trong danh mục này.
              </div>
            </v-card>

            <v-card class="surface-card pa-6">
              <div class="section-heading mb-2">Workflow</div>
              <h2 class="text-h5 font-weight-bold mb-3">Vì sao giao diện này khác?</h2>
              <p class="muted-copy mb-0">
                Màn chi tiết được rút về đúng dữ liệu backend có thật, không dựa trên biến thể,
                giá hay tồn kho giả. Điều này giúp UI không hứa những nghiệp vụ mà API hiện chưa có.
              </p>
            </v-card>
          </v-col>
        </v-row>
      </template>
    </v-container>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

import { useProductsStore } from '@/stores/products.store'

const route = useRoute()
const productsStore = useProductsStore()

const productId = computed(() => Number(route.query.id))
const currentProduct = computed(() => productsStore.currentProduct)
const relatedProducts = computed(() =>
  productsStore.products
    .filter((product) => product.categoryId === currentProduct.value?.categoryId && product.id !== currentProduct.value?.id)
    .slice(0, 4),
)

const copySlug = async () => {
  if (!currentProduct.value?.slug) return
  await navigator.clipboard.writeText(currentProduct.value.slug)
}

onMounted(async () => {
  if (productId.value) {
    await productsStore.fetchProductById(productId.value)
  }
  if (productsStore.products.length === 0) {
    await productsStore.fetchProducts()
  }
})
</script>
