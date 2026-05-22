import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

import productsApi from '@/api/products.api'
import type { ProductDetail, ProductResponse } from '@/types'

export const useProductsStore = defineStore('products', () => {
  const products = ref<ProductResponse[]>([])
  const currentProduct = ref<ProductDetail | null>(null)
  const isLoading = ref(false)
  const error = ref('')
  const total = ref(0)

  const hasProducts = computed(() => products.value.length > 0)
  const productCount = computed(() => products.value.length)

  const normalizeProduct = (item: ProductResponse): ProductResponse => ({
    ...item,
    id: item.id ?? item.Id ?? 0,
    name: item.name ?? item.Name ?? '',
    slug: item.slug ?? item.Slug ?? '',
    description: item.description ?? item.Description ?? '',
    isVaulted: item.isVaulted ?? item.IsVaulted ?? false,
    categoryId: item.categoryId ?? item.category_id ?? item.CategoryId ?? null,
    categoryName: item.categoryName ?? item.category_name ?? item.CategoryName ?? null,
    Id: item.id ?? item.Id ?? 0,
    Name: item.name ?? item.Name ?? '',
    Slug: item.slug ?? item.Slug ?? '',
    Description: item.description ?? item.Description ?? '',
    IsVaulted: item.isVaulted ?? item.IsVaulted ?? false,
    CategoryId: item.categoryId ?? item.category_id ?? item.CategoryId ?? null,
    CategoryName: item.categoryName ?? item.category_name ?? item.CategoryName ?? null,
  })

  const normalizeList = (items: ProductResponse[]) => items.map(normalizeProduct)

  const unwrapProducts = (payload: unknown) => {
    if (Array.isArray(payload)) return payload as ProductResponse[]
    return []
  }

  const fetchProducts = async () => {
    isLoading.value = true
    error.value = ''

    try {
      const response = await productsApi.getAll()
      const payload = response.data?.result
      const items = normalizeList(unwrapProducts(payload))
      products.value = items
      total.value = items.length
    } catch (err) {
      error.value = (err as Error).message || 'Không thể tải sản phẩm'
      products.value = []
      total.value = 0
    } finally {
      isLoading.value = false
    }
  }

  const fetchProductById = async (id: number) => {
    isLoading.value = true
    error.value = ''
    currentProduct.value = null

    try {
      const response = await productsApi.getById(id)
      const payload = response.data?.result
      if (payload && typeof payload === 'object') {
        currentProduct.value = normalizeProduct(payload as ProductResponse)
      }
    } catch (err) {
      error.value = (err as Error).message || 'Không thể tải chi tiết sản phẩm'
    } finally {
      isLoading.value = false
    }
  }

  const fetchByCategory = async (categoryId: number) => {
    isLoading.value = true
    error.value = ''

    try {
      const response = await productsApi.getByCategory(categoryId)
      const payload = response.data?.result
      const items = normalizeList(unwrapProducts(payload))
      products.value = items
      total.value = items.length
    } catch (err) {
      error.value = (err as Error).message || 'Không thể tải sản phẩm theo danh mục'
      products.value = []
      total.value = 0
    } finally {
      isLoading.value = false
    }
  }

  const filterLocal = (predicate: (product: ProductResponse) => boolean) => {
    return products.value.filter(predicate)
  }

  return {
    products,
    currentProduct,
    isLoading,
    error,
    total,
    hasProducts,
    productCount,
    fetchProducts,
    fetchProductById,
    fetchByCategory,
    filterLocal,
  }
})
