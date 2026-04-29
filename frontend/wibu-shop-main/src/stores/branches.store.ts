/**
 * Branches Store
 * Quản lý state của chi nhánh và tồn kho
 */
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import branchesApi from '@/api/branches.api';
import { mockBranches, getMockStocksByVariant, getMockStocksByProduct } from '@/data/mockCatalog';

export interface Branch {
    Id: number;
    Name: string;
    Location?: string;
    Address?: string;
    Phone?: string;
}

export interface BranchStock {
    BranchId: number;
    BranchName: string;
    Location?: string;
    VariantId: number;
    VariantName?: string;
    Stock: number;
}

export const useBranchesStore = defineStore('branches', () => {
    // ========== STATE ==========
    const branches = ref<Branch[]>([]);
    const variantStocks = ref<BranchStock[]>([]);
    const isLoading = ref(false);
    const error = ref('');

    // ========== GETTERS ==========
    const hasBranches = computed(() => branches.value.length > 0);

    // Lấy danh sách chi nhánh có hàng
    const availableBranches = computed(() =>
        variantStocks.value.filter(s => s.Stock > 0)
    );

    // Tổng tồn kho của variant hiện tại
    const totalStock = computed(() =>
        variantStocks.value.reduce((sum, s) => sum + s.Stock, 0)
    );

    // ========== ACTIONS ==========

    /**
     * Lấy danh sách tất cả chi nhánh
     */
    const fetchBranches = async () => {
        if (branches.value.length > 0) return;

        isLoading.value = true;
        error.value = '';
        try {
            const response = await branchesApi.getAll();
            const data = response.data?.data ?? response.data ?? [];
            branches.value = Array.isArray(data) && data.length > 0
                ? data.map((item: any) => ({
                    Id: item.id ?? item.Id,
                    Name: item.name ?? item.Name,
                    Location: item.location ?? item.Location,
                    Address: item.location ?? item.Location,
                }))
                : mockBranches;
        } catch {
            branches.value = mockBranches;
        }
        isLoading.value = false;
    };

    /**
     * Lấy tồn kho theo variant ID
     */
    const fetchStockByVariant = async (variantId: number) => {
        isLoading.value = true;
        error.value = '';
        variantStocks.value = [];
        try {
            const response = await branchesApi.getStockByVariant(variantId);
            const data = response.data?.data ?? response.data ?? [];
            variantStocks.value = Array.isArray(data) && data.length > 0
                ? data.map((item: any) => ({
                    BranchId: item.branchId ?? item.BranchId,
                    BranchName: item.branchName ?? item.BranchName,
                    Location: item.location ?? item.Location,
                    VariantId: item.variantId ?? item.VariantId,
                    VariantName: item.variantName ?? item.VariantName,
                    Stock: item.stock ?? item.Stock ?? 0,
                }))
                : getMockStocksByVariant(variantId);
        } catch {
            variantStocks.value = getMockStocksByVariant(variantId);
        }
        isLoading.value = false;
    };

    /**
     * Lấy tồn kho theo product ID (tất cả variants)
     */
    const fetchStockByProduct = async (productId: number) => {
        isLoading.value = true;
        error.value = '';
        try {
            const response = await branchesApi.getStockByProduct(productId);
            const data = response.data?.data ?? response.data ?? [];
            variantStocks.value = Array.isArray(data) && data.length > 0
                ? data.map((item: any) => ({
                    BranchId: item.branchId ?? item.BranchId,
                    BranchName: item.branchName ?? item.BranchName,
                    Location: item.location ?? item.Location,
                    VariantId: item.variantId ?? item.VariantId,
                    VariantName: item.variantName ?? item.VariantName,
                    Stock: item.stock ?? item.Stock ?? 0,
                }))
                : getMockStocksByProduct(productId);
        } catch {
            variantStocks.value = getMockStocksByProduct(productId);
        }
        isLoading.value = false;
    };

    /**
     * Lọc tồn kho theo variant ID từ data đã có
     */
    const getStockForVariant = (variantId: number) => {
        return variantStocks.value.filter(s => s.VariantId === variantId);
    };

    /**
     * Clear stock data
     */
    const clearStocks = () => {
        variantStocks.value = [];
    };

    return {
        // State
        branches,
        variantStocks,
        isLoading,
        error,
        // Getters
        hasBranches,
        availableBranches,
        totalStock,
        // Actions
        fetchBranches,
        fetchStockByVariant,
        fetchStockByProduct,
        getStockForVariant,
        clearStocks,
    };
});
