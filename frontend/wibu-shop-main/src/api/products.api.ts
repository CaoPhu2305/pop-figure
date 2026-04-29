import apiClient from '@/api/index.ts';

export const productsApi = {
  /**
   * Lấy tất cả sản phẩm
   */
  getAll: (params?: {
    search?: string;
    categoryId?: number;
    page?: number;
    size?: number;
  }) => {
    return apiClient.get('/products', { params });
  },

  /**
   * Lấy sản phẩm theo ID
   */
  getById: (id: number) => {
    return apiClient.get(`/products/${id}`);
  },

  /**
   * Lấy sản phẩm theo category
   */
  getByCategory: (categoryId: number) => {
    return apiClient.get('/products', {
      params: { categoryId },
    });
  },

  /**
   * Tìm kiếm sản phẩm
   */
  search: (keyword: string) => {
    return apiClient.get('/products', {
      params: { search: keyword }
    });
  },
};

export default productsApi;

