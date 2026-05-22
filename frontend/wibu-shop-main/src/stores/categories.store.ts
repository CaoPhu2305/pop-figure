import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

import categoriesApi from '@/api/categories.api'
import type { CategoryResponse } from '@/types'

const fallbackCategories: CategoryResponse[] = []

export const useCategoriesStore = defineStore('categories', () => {
  const categories = ref<CategoryResponse[]>([])
  const isLoading = ref(false)
  const error = ref('')

  const hasCategories = computed(() => categories.value.length > 0)

  const categoryOptions = computed(() =>
    categories.value.map((category) => ({
      id: category.id,
      name: category.name,
    })),
  )

  const normalize = (items: CategoryResponse[]) =>
    items.map((item) => ({
      ...item,
      id: item.id ?? item.Id ?? 0,
      name: item.name ?? item.Name ?? '',
      slug: item.slug ?? item.Slug ?? '',
      Id: item.id ?? item.Id ?? 0,
      Name: item.name ?? item.Name ?? '',
      Slug: item.slug ?? item.Slug ?? '',
    }))

  const fetchCategories = async () => {
    if (categories.value.length > 0) return

    isLoading.value = true
    error.value = ''

    try {
      const response = await categoriesApi.getAll()
      const payload = response.data?.result
      const items = Array.isArray(payload) ? normalize(payload as CategoryResponse[]) : []
      categories.value = items.length > 0 ? items : fallbackCategories
    } catch (err) {
      error.value = (err as Error).message || 'Không thể tải danh mục'
      categories.value = fallbackCategories
    } finally {
      isLoading.value = false
    }
  }

  return {
    categories,
    isLoading,
    error,
    hasCategories,
    categoryOptions,
    fetchCategories,
  }
})
