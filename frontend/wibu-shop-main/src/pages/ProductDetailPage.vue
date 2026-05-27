<template>
  <div class="product-detail-page">
    <v-container class="py-10">
      <v-alert v-if="!productId" type="info" variant="tonal" class="mb-6">
        Không tìm thấy mã sản phẩm hợp lệ. Hãy quay lại trang danh sách để chọn sản phẩm khác.
      </v-alert>

      <v-skeleton-loader
        v-if="productsStore.isLoading && !currentProduct"
        class="surface-card pa-8"
        type="article, image, actions"
      />

      <template v-else-if="currentProduct">
        <v-row class="gy-6">
          <v-col cols="12" lg="7">
            <v-card class="surface-card pa-6 h-100">
              <v-img :src="getProductImage(currentProduct.id, currentProduct.slug)" height="360" cover class="mb-5 rounded-lg" />

              <div class="d-flex flex-wrap ga-2 mb-4">
                <v-chip color="primary" variant="tonal">{{ currentProduct.categoryName || 'Chưa phân loại' }}</v-chip>
                <v-chip v-if="currentProduct.isVaulted" color="warning" variant="tonal">Limited Edition</v-chip>
                <v-chip variant="outlined">{{ currentProduct.slug }}</v-chip>
              </div>

              <h1 class="text-h4 text-md-h3 font-weight-black mb-3">{{ currentProduct.name }}</h1>
              <p class="text-body-1 muted-copy mb-6">{{ currentProduct.description }}</p>

              <div class="d-flex flex-wrap ga-3">
                <v-btn color="primary" to="/products">Quay lại danh sách sản phẩm</v-btn>
                <v-btn variant="outlined" @click="copySlug">Sao chép mã sản phẩm</v-btn>
              </div>
            </v-card>
          </v-col>

          <v-col cols="12" lg="5">
            <v-card class="surface-card pa-6 mb-6">
              <div class="section-heading mb-2">Thông tin mua sắm</div>
              <h2 class="text-h5 font-weight-bold mb-4">Cam kết tại PopFigure</h2>
              <v-list class="pa-0" lines="two">
                <v-list-item title="Kiểm tra sản phẩm trước khi đóng gói" subtitle="Đảm bảo tình trạng hộp và phụ kiện" prepend-icon="mdi-check-decagram-outline" />
                <v-list-item title="Đóng gói chống sốc nhiều lớp" subtitle="Hạn chế tối đa rủi ro trong quá trình vận chuyển" prepend-icon="mdi-package-variant-closed" />
                <v-list-item title="Hỗ trợ tư vấn lựa chọn" subtitle="Giúp bạn chọn đúng dòng figure phù hợp" prepend-icon="mdi-headset" />
              </v-list>
            </v-card>

            <v-card class="surface-card pa-6">
              <div class="section-heading mb-2">Sản phẩm liên quan</div>
              <h2 class="text-h5 font-weight-bold mb-4">Cùng danh mục</h2>
              <v-list class="pa-0" lines="two">
                <template v-for="item in relatedProducts" :key="item.id">
                  <v-divider />
                  <v-list-item :to="`/products/${item.id}`" :title="item.name" :subtitle="item.slug">
                    <template #append>
                      <v-chip size="small" variant="tonal">#{{ item.id }}</v-chip>
                    </template>
                  </v-list-item>
                </template>
              </v-list>
              <div v-if="relatedProducts.length === 0" class="text-center py-6 muted-copy">
                Chưa có sản phẩm liên quan trong danh mục này.
              </div>
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

const productId = computed(() => Number(route.params.id))
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

const getProductImage = (id: number, slug: string) => {
  return `https://picsum.photos/seed/detail-${id}-${slug}/920/680`
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
