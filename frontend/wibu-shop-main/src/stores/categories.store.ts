/**
 * Categories Store
 * Quản lý state của danh mục
 */
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import categoriesApi from '@/api/categories.api';
import { mockCategories } from '@/data/mockCatalog';

export interface Category {
    Id: number;
    Name: string;
    Slug?: string;
    Description?: string;
}

export const useCategoriesStore = defineStore('categories', () => {
    // ========== STATE ==========
    const categories = ref<Category[]>([]);
    const isLoading = ref(false);
    const error = ref('');

    // ========== GETTERS ==========
    const hasCategories = computed(() => categories.value.length > 0);

    // Chuyển đổi cho v-select (id, name)
    const categoryOptions = computed(() =>
        categories.value.map(c => ({
            id: c.Id,
            name: c.Name
        }))
    );

    // ========== ACTIONS ==========
    const fetchCategories = async () => {
        // Không fetch lại nếu đã có data
        if (categories.value.length > 0) return;

        isLoading.value = true;
        error.value = '';
        try {
            const response = await categoriesApi.getAll();
            const data = response.data?.data ?? response.data ?? [];
            categories.value = Array.isArray(data) && data.length > 0
                ? data.map((item: any) => ({
                    Id: item.id ?? item.Id,
                    Name: item.name ?? item.Name,
                    Slug: item.slug ?? item.Slug,
                    Description: item.description ?? item.Description,
                }))
                : mockCategories;
        } catch {
            categories.value = mockCategories;
        }
        isLoading.value = false;
    };

    return {
        // State
        categories,
        isLoading,
        error,
        // Getters
        hasCategories,
        categoryOptions,
        // Actions
        fetchCategories,
    };
});
