<template>
  <div class="product-list-page">
    <v-container class="py-10">
      <v-card class="surface-card pa-6 mb-6">
        <v-row align="center" class="gy-4">
          <v-col cols="12" lg="7">
            <div class="section-heading mb-2">Product Catalog</div>
            <h1 class="text-h4 text-md-h3 font-weight-black mb-3">Danh sách sản phẩm nổi bật</h1>
            <p class="muted-copy mb-0">
              Khám phá nhiều mẫu figure và collectible theo danh mục, tìm kiếm nhanh theo tên sản phẩm.
            </p>
          </v-col>
          <v-col cols="12" lg="5">
            <v-text-field
              v-model="searchQuery"
              prepend-inner-icon="mdi-magnify"
              label="Tìm kiếm sản phẩm"
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
          <v-col cols="12" md="3">
            <div class="text-caption muted-copy">Kết quả hiện tại</div>
            <div class="text-h5 font-weight-bold">{{ filteredProducts.length }} sản phẩm</div>
            <div class="text-caption muted-copy">Trang {{ currentPage }} / {{ totalPages }}</div>
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
              v-model="availabilityFilter"
              :items="availabilityOptions"
              item-title="title"
              item-value="value"
              label="Trạng thái"
              variant="outlined"
              rounded="lg"
              hide-details
            />
          </v-col>
          <v-col cols="12" sm="6" md="2">
            <v-select
              v-model="sortBy"
              :items="sortOptions"
              item-title="title"
              item-value="value"
              label="Sắp xếp"
              variant="outlined"
              rounded="lg"
              hide-details
            />
          </v-col>
          <v-col cols="12" sm="6" md="1" class="d-flex justify-end">
            <v-btn variant="outlined" rounded="xl" @click="resetFilters">Đặt lại</v-btn>
          </v-col>
        </v-row>
      </v-card>

      <v-row v-if="productsStore.isLoading">
        <v-col v-for="n in 8" :key="n" cols="12" sm="6" lg="3">
          <v-skeleton-loader class="surface-card" type="image, article" />
        </v-col>
      </v-row>

      <v-row v-else>
        <v-col v-for="product in pagedProducts" :key="product.id" cols="12" sm="6" lg="3">
          <v-card class="surface-card h-100 product-card" :to="`/products/${product.id}`">
            <v-img
              :src="getProductImage(product)"
              height="180"
              cover
              class="product-image"
            />
            <v-card-text>
              <div class="d-flex align-center justify-space-between mb-3">
                <v-chip size="small" color="primary" variant="tonal">
                  {{ product.categoryName || 'Chưa phân loại' }}
                </v-chip>
                <v-chip v-if="product.isVaulted" size="small" color="warning" variant="tonal">Limited</v-chip>
              </div>
              <h3 class="text-subtitle-1 font-weight-bold mb-2 line-clamp-2">{{ product.name }}</h3>
              <p class="muted-copy line-clamp-2 mb-3">{{ product.description }}</p>
              <div class="d-flex justify-space-between align-center">
                <span class="text-caption muted-copy">{{ product.slug }}</span>
                <v-btn color="primary" variant="text" size="small">Xem chi tiết</v-btn>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-card v-if="!productsStore.isLoading && filteredProducts.length === 0" class="surface-card pa-10 text-center">
        <v-icon size="64" color="primary" class="mb-4">mdi-package-variant-off</v-icon>
        <h2 class="text-h5 font-weight-bold mb-2">Không tìm thấy sản phẩm phù hợp</h2>
        <p class="muted-copy mb-0">Bạn thử đổi từ khóa hoặc chọn danh mục khác nhé.</p>
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
const availabilityFilter = ref<'all' | 'onsale'>('onsale')
const sortBy = ref<'newest' | 'az' | 'za'>('newest')
const currentPage = ref(1)
const pageSize = 12

const categoryOptions = computed(() => categoriesStore.categoryOptions)
const availabilityOptions = [
  { title: 'Đang bán', value: 'onsale' },
  { title: 'Tất cả sản phẩm', value: 'all' },
]
const sortOptions = [
  { title: 'Mới nhất', value: 'newest' },
  { title: 'A → Z', value: 'az' },
  { title: 'Z → A', value: 'za' },
]

const filteredProducts = computed(() => {
  const keyword = searchQuery.value.trim().toLowerCase()

  const base = productsStore.products.filter((product: ProductResponse) => {
    const matchesKeyword =
      !keyword ||
      product.name.toLowerCase().includes(keyword) ||
      product.slug.toLowerCase().includes(keyword) ||
      product.description.toLowerCase().includes(keyword)

    const matchesCategory = !categoryFilter.value || product.categoryId === categoryFilter.value
    const matchesAvailability = availabilityFilter.value === 'all' || !product.isVaulted

    return matchesKeyword && matchesCategory && matchesAvailability
  })

  return base.sort((a, b) => {
    if (sortBy.value === 'az') return a.name.localeCompare(b.name)
    if (sortBy.value === 'za') return b.name.localeCompare(a.name)
    return b.id - a.id
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredProducts.value.length / pageSize)))

const pagedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredProducts.value.slice(start, start + pageSize)
})

const updateQuery = () => {
  router.replace({
    query: {
      search: searchQuery.value || undefined,
      category: categoryFilter.value || undefined,
    },
  })
}

const resetFilters = () => {
  searchQuery.value = ''
  categoryFilter.value = null
  availabilityFilter.value = 'onsale'
  sortBy.value = 'newest'
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

watch([searchQuery, categoryFilter], () => {
  currentPage.value = 1
  updateQuery()
})

watch([availabilityFilter, sortBy], () => {
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
