<template>
  <div class="product-list-page">
    <v-container class="py-10">
      <v-card class="surface-card pa-6 mb-6">
        <v-row align="center" class="gy-4">
          <v-col cols="12" lg="7">
            <div class="section-heading mb-2">Catalog</div>
            <h1 class="text-h4 text-md-h3 font-weight-black mb-3">
              Danh sách sản phẩm theo cấu trúc database
            </h1>
            <p class="muted-copy mb-0">
              Bộ lọc và lưới hiển thị này chỉ dùng các field thực tế từ backend: name, slug, description,
              category và trạng thái vaulted.
            </p>
          </v-col>
          <v-col cols="12" lg="5">
            <v-text-field
              v-model="searchQuery"
              prepend-inner-icon="mdi-magnify"
              label="Tìm theo tên hoặc slug"
              variant="outlined"
              rounded="xl"
              hide-details
              clearable
            />
          </v-col>
        </v-row>
      </v-card>

      <v-card class="surface-card pa-4 mb-6">
        <v-row align="center" dense>
          <v-col cols="12" md="4">
            <div class="text-caption muted-copy">Tổng sản phẩm</div>
            <div class="text-h5 font-weight-bold">{{ filteredProducts.length }}</div>
            <div class="text-caption muted-copy">
              Trang {{ currentPage }} / {{ totalPages }} - {{ pageSize }} sản phẩm mỗi trang
            </div>
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <v-select
              v-model="categoryFilter"
              :items="categoryOptions"
              item-title="name"
              item-value="id"
              label="Danh mục"
              variant="outlined"
              rounded="lg"
              hide-details
              clearable
            />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <v-select
              v-model="vaultedFilter"
              :items="vaultedOptions"
              item-title="title"
              item-value="value"
              label="Trạng thái"
              variant="outlined"
              rounded="lg"
              hide-details
            />
          </v-col>
          <v-col cols="12" md="2" class="d-flex justify-end">
            <v-btn variant="outlined" rounded="xl" @click="resetFilters">Xóa lọc</v-btn>
          </v-col>
        </v-row>
      </v-card>

      <v-row v-if="productsStore.isLoading">
        <v-col v-for="n in 6" :key="n" cols="12" sm="6" lg="4">
          <v-skeleton-loader class="surface-card" type="image, article, actions" />
        </v-col>
      </v-row>

      <v-row v-else>
        <v-col v-for="product in pagedProducts" :key="product.id" cols="12" sm="6" lg="4">
          <v-card class="surface-card h-100 product-card">
            <v-img
              :src="getProductImage(product)"
              height="190"
              cover
              class="product-image"
            />
            <v-card-text>
              <div class="d-flex align-center justify-space-between mb-3">
                <v-chip size="small" color="primary" variant="tonal">
                  {{ product.categoryName || 'Chưa phân loại' }}
                </v-chip>
                <v-chip v-if="product.isVaulted" size="small" color="warning" variant="tonal">
                  Vaulted
                </v-chip>
              </div>
              <h3 class="text-h6 font-weight-bold mb-2">{{ product.name }}</h3>
              <p class="muted-copy line-clamp-2 mb-4">{{ product.description }}</p>
              <div class="d-flex justify-space-between align-center">
                <div class="text-caption muted-copy">Slug: {{ product.slug }}</div>
                <v-btn :to="`/product?id=${product.id}`" color="primary" variant="text">Xem chi tiết</v-btn>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-card v-if="!productsStore.isLoading && filteredProducts.length === 0" class="surface-card pa-10 text-center">
        <v-icon size="64" color="primary" class="mb-4">mdi-package-variant-off</v-icon>
        <h2 class="text-h5 font-weight-bold mb-2">Không tìm thấy sản phẩm phù hợp</h2>
        <p class="muted-copy mb-0">Hãy thử đổi từ khóa hoặc bỏ bớt bộ lọc.</p>
      </v-card>

      <div v-if="!productsStore.isLoading && filteredProducts.length > 0" class="d-flex justify-center mt-8">
        <v-pagination
          v-model="currentPage"
          :length="totalPages"
          rounded="circle"
          color="primary"
          density="comfortable"
        />
      </div>
    </v-container>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { useCategoriesStore } from '@/stores/categories.store'
import { useProductsStore } from '@/stores/products.store'
import type { ProductResponse } from '@/types'

const route = useRoute()
const router = useRouter()
const productsStore = useProductsStore()
const categoriesStore = useCategoriesStore()

const searchQuery = ref(String(route.query.search ?? ''))
const categoryFilter = ref<number | null>(route.query.category ? Number(route.query.category) : null)
const vaultedFilter = ref<'all' | 'vaulted' | 'plain'>('all')
const currentPage = ref(1)
const pageSize = 9

const categoryOptions = computed(() => categoriesStore.categoryOptions)
const vaultedOptions = [
  { title: 'Tất cả', value: 'all' },
  { title: 'Chỉ vaulted', value: 'vaulted' },
  { title: 'Không vaulted', value: 'plain' },
]

const filteredProducts = computed(() => {
  const keyword = searchQuery.value.trim().toLowerCase()
  return productsStore.products.filter((product: ProductResponse) => {
    const matchesKeyword =
      !keyword ||
      product.name.toLowerCase().includes(keyword) ||
      product.slug.toLowerCase().includes(keyword) ||
      product.description.toLowerCase().includes(keyword)

    const matchesCategory = !categoryFilter.value || product.categoryId === categoryFilter.value
    const matchesVaulted =
      vaultedFilter.value === 'all' ||
      (vaultedFilter.value === 'vaulted' && Boolean(product.isVaulted)) ||
      (vaultedFilter.value === 'plain' && !product.isVaulted)

    return matchesKeyword && matchesCategory && matchesVaulted
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredProducts.value.length / pageSize)))

const pagedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredProducts.value.slice(start, start + pageSize)
})

const resetFilters = () => {
  searchQuery.value = ''
  categoryFilter.value = null
  vaultedFilter.value = 'all'
  currentPage.value = 1
  router.replace({ query: {} })
}

watch(
  () => route.query.search,
  (value) => {
    searchQuery.value = String(value ?? '')
  },
)

watch(
  () => route.query.category,
  (value) => {
    categoryFilter.value = value ? Number(value) : null
  },
)

watch(searchQuery, (value) => {
  currentPage.value = 1
  router.replace({
    query: {
      ...route.query,
      search: value || undefined,
      category: categoryFilter.value || undefined,
    },
  })
})

watch([categoryFilter, vaultedFilter], () => {
  currentPage.value = 1
})

watch(totalPages, (value) => {
  if (currentPage.value > value) {
    currentPage.value = value
  }
})

const getProductImage = (product: { id: number; slug: string }) => {
  return `https://picsum.photos/seed/catalog-${product.id}-${product.slug}/720/480`
}

onMounted(async () => {
  await Promise.all([categoriesStore.fetchCategories(), productsStore.fetchProducts()])
})
</script>

<style scoped>
.product-card {
  transition: transform 0.22s ease, box-shadow 0.22s ease;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 18px 40px rgba(44, 33, 28, 0.08);
}

.product-image {
  border-bottom: 1px solid rgba(92, 66, 48, 0.08);
}
</style>
