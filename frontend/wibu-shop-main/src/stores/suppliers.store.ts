/**
 * Suppliers Store
 * Quản lý state của nhà cung cấp
 */
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import suppliersApi from '@/api/suppliers.api';
import { mockSuppliers } from '@/data/mockCatalog';

export interface Supplier {
    Id: number;
    Name: string;
    ContactInfo?: string;
}

export const useSuppliersStore = defineStore('suppliers', () => {
    // ========== STATE ==========
    const suppliers = ref<Supplier[]>([]);
    const isLoading = ref(false);
    const error = ref('');

    // ========== GETTERS ==========
    const hasSuppliers = computed(() => suppliers.value.length > 0);

    // Chuyển đổi cho v-select (id, name)
    const supplierOptions = computed(() =>
        suppliers.value.map(s => ({
            id: s.Id,
            name: s.Name
        }))
    );

    // ========== ACTIONS ==========
    const fetchSuppliers = async () => {
        // Không fetch lại nếu đã có data
        if (suppliers.value.length > 0) return;

        isLoading.value = true;
        error.value = '';
        try {
            const response = await suppliersApi.getAll();
            const data = response.data?.data ?? response.data ?? [];
            suppliers.value = Array.isArray(data) && data.length > 0
                ? data.map((item: any) => ({
                    Id: item.id ?? item.Id,
                    Name: item.name ?? item.Name,
                    ContactInfo: item.contactInfo ?? item.contact_info ?? item.ContactInfo,
                }))
                : mockSuppliers;
        } catch {
            suppliers.value = mockSuppliers;
        }
        isLoading.value = false;
    };

    return {
        // State
        suppliers,
        isLoading,
        error,
        // Getters
        hasSuppliers,
        supplierOptions,
        // Actions
        fetchSuppliers,
    };
});
