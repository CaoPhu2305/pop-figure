import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Product, ProductDetail } from '@/types/index.ts';
import productsApi from '@/api/products.api';
import { cloneMockList, cloneMockProductById, getMockProductsByCategory, searchMockProducts } from '@/data/mockCatalog';

type ApiVariant = {
  id?: number;
  Id?: number;
  name?: string;
  Name?: string;
  sku?: string;
  Sku?: string;
  price?: number;
  Price?: number;
  original_price?: number | null;
  OriginalPrice?: number | null;
  image_url?: string | null;
  ImageUrl?: string | null;
};

type ApiProductListItem = {
  id?: number;
  Id?: number;
  name?: string;
  Name?: string;
  description?: string | null;
  Description?: string | null;
  slug?: string;
  Slug?: string;
  status?: string;
  Status?: string;
  categoryId?: number | null;
  category_id?: number | null;
  categoryName?: string | null;
  CategoryName?: string | null;
  supplierId?: number | null;
  supplier_id?: number | null;
  supplierName?: string | null;
  SupplierName?: string | null;
  minPrice?: number | null;
  maxPrice?: number | null;
  imageUrl?: string | null;
  ImageUrl?: string | null;
};

type ApiProductDetail = ApiProductListItem & {
  category?: { id?: number; Id?: number; name?: string; Name?: string; slug?: string; Slug?: string };
  Category?: { id?: number; Id?: number; name?: string; Name?: string; slug?: string; Slug?: string };
  supplier?: { id?: number; Id?: number; name?: string; Name?: string };
  Supplier?: { id?: number; Id?: number; name?: string; Name?: string };
  variants?: ApiVariant[];
  Variants?: ApiVariant[];
  images?: string[];
  Images?: string[];
  rating?: number;
  Rating?: number;
  reviewCount?: number;
  ReviewCount?: number;
  soldCount?: number;
  SoldCount?: number;
};

export const useProductsStore = defineStore('products', () => {

    const products = ref<Product[]>([]);
    const currentProduct = ref<ProductDetail | null>(null);
    const isLoading = ref(false);
    const error = ref('');
    const total = ref(0);

    const hasProducts = computed(() => products.value.length > 0);
    const getProductCount = computed(() => products.value.length);

    const applyMockProducts = (items: Product[] = cloneMockList()) => {
      products.value = items;
      total.value = items.length;
      error.value = '';
    };

    const applyMockProductDetail = (id: number) => {
      const product = cloneMockProductById(id);
      if (product) {
        currentProduct.value = product as unknown as ProductDetail;
        error.value = '';
        return true;
      }

      return false;
    };

    const fetchProducts = async () => {
      isLoading.value = true;
      error.value = '';

      try {
        const response = await productsApi.getAll();
        const payload = response.data?.data ?? response.data;
        const items = normalizeProductList(payload);
        if (items.length > 0) {
          products.value = items;
          total.value = Number(extractTotal(payload, items.length));
        } else {
          applyMockProducts();
        }
      } catch {
        applyMockProducts();
      } finally {
        isLoading.value = false;
      }
    };

    const fetchProductById = async (id: number) => {
      isLoading.value = true;
      error.value = '';
      currentProduct.value = null;

      try {
        const response = await productsApi.getById(id);
        const payload = response.data?.data ?? response.data;
        const normalized = normalizeProductDetail(payload);
        if (normalized) {
          currentProduct.value = normalized as unknown as ProductDetail;
        } else if (!applyMockProductDetail(id)) {
          applyMockProductDetail(101);
        }
      } catch {
        if (!applyMockProductDetail(id)) {
          applyMockProductDetail(101);
        }
      } finally {
        isLoading.value = false;
      }
    };

    const fetchByCategory = async (categoryId: number) => {
      isLoading.value = true;
      error.value = '';

      try {
        const response = await productsApi.getByCategory(categoryId);
        const payload = response.data?.data ?? response.data;
        const items = normalizeProductList(payload);
        if (items.length > 0) {
          products.value = items;
          total.value = Number(extractTotal(payload, items.length));
        } else {
          products.value = getMockProductsByCategory(categoryId);
          total.value = products.value.length;
        }
      } catch {
        products.value = getMockProductsByCategory(categoryId);
        total.value = products.value.length;
      } finally {
        isLoading.value = false;
      }
    };

    const searchProducts = async (keyword: string) => {
      if (!keyword.trim()) {
        products.value = [];
        total.value = 0;
        return;
      }

      isLoading.value = true;
      error.value = '';

      try {
        const response = await productsApi.search(keyword);
        const payload = response.data?.data ?? response.data;
        const items = normalizeProductList(payload);
        if (items.length > 0) {
          products.value = items;
          total.value = Number(extractTotal(payload, items.length));
        } else {
          products.value = searchMockProducts(keyword);
          total.value = products.value.length;
        }
      } catch {
        products.value = searchMockProducts(keyword);
        total.value = products.value.length;
      } finally {
        isLoading.value = false;
      }
    };

  const clearCurrentProduct = () => {
    currentProduct.value = null;
  };

  return {
    // State
    products,
    currentProduct,
    isLoading,
    error,
    total,
    // Getters
    hasProducts,
    getProductCount,
    // Actions
    fetchProducts,
    fetchProductById,
    fetchByCategory,
    searchProducts,
    clearCurrentProduct,
  };

  function normalizeProductList(payload: unknown): Product[] {
    const items = Array.isArray(payload)
      ? payload
      : Array.isArray((payload as { content?: unknown[] } | null)?.content)
        ? (payload as { content: unknown[] }).content
        : [];

    return items.map((item: ApiProductListItem) => ({
      Id: item.id ?? item.Id ?? 0,
      Name: item.name ?? item.Name ?? '',
      Slug: item.slug ?? item.Slug ?? '',
      Description: item.description ?? item.Description ?? '',
      CategoryId: item.categoryId ?? item.category_id ?? undefined,
      CategoryName: item.categoryName ?? item.CategoryName ?? undefined,
      Price: Number(item.minPrice ?? item.maxPrice ?? 0),
      OriginalPrice: item.maxPrice ?? undefined,
      ImageUrl: item.imageUrl ?? item.ImageUrl ?? undefined,
    }));
  }

  function extractTotal(payload: unknown, fallback: number): number {
    if (payload && typeof payload === 'object' && 'totalElements' in payload) {
      const total = Number((payload as { totalElements?: number }).totalElements);
      if (!Number.isNaN(total)) {
        return total;
      }
    }

    if (payload && typeof payload === 'object' && 'total' in payload) {
      const total = Number((payload as { total?: number }).total);
      if (!Number.isNaN(total)) {
        return total;
      }
    }

    return fallback;
  }

  function normalizeProductDetail(payload: unknown): ProductDetail | null {
    if (!payload || typeof payload !== 'object') return null;
    const item = payload as ApiProductDetail;
    const variants = (item.variants ?? item.Variants ?? []).map((variant: ApiVariant) => ({
      Id: variant.id ?? variant.Id ?? 0,
      Name: variant.name ?? variant.Name ?? 'Standard',
      Sku: variant.sku ?? variant.Sku ?? '',
      Price: Number(variant.price ?? variant.Price ?? 0),
      OriginalPrice: variant.original_price ?? variant.OriginalPrice ?? undefined,
      ImageUrl: variant.image_url ?? variant.ImageUrl ?? undefined,
    }));

    return {
      Id: item.id ?? item.Id ?? 0,
      Name: item.name ?? item.Name ?? '',
      Slug: item.slug ?? item.Slug ?? '',
      Description: item.description ?? item.Description ?? '',
      CategoryId: item.categoryId ?? item.category_id ?? item.Category?.Id ?? item.category?.id,
      CategoryName: item.categoryName ?? item.CategoryName ?? item.Category?.Name ?? item.category?.name,
      Price: Number(item.minPrice ?? item.maxPrice ?? 0),
      OriginalPrice: item.maxPrice ?? undefined,
      ImageUrl: item.imageUrl ?? item.ImageUrl ?? undefined,
      Category: {
        Id: item.Category?.Id ?? item.category?.id ?? 0,
        Name: item.Category?.Name ?? item.category?.name ?? item.categoryName ?? item.CategoryName ?? '',
      },
      Supplier: {
        Id: item.Supplier?.Id ?? item.supplier?.id ?? 0,
        Name: item.Supplier?.Name ?? item.supplier?.name ?? item.SupplierName ?? item.supplierName ?? '',
      },
      SupplierName: item.SupplierName ?? item.supplierName ?? item.Supplier?.Name ?? item.supplier?.name,
      Status: item.status ?? item.Status ?? 'active',
      Variants: variants,
      Images: item.images ?? item.Images ?? [],
      Rating: item.rating ?? item.Rating ?? 0,
      ReviewCount: item.reviewCount ?? item.ReviewCount ?? 0,
      SoldCount: item.soldCount ?? item.SoldCount ?? 0,
    } as ProductDetail;
  }


});
