// Types aligned with the current PopFigure backend.
// Legacy alias fields are kept on many interfaces so the older components
// continue to compile while we move the UI over to the real API shape.

// ========== API ENVELOPE ==========
export interface ApiResponse<T> {
  code?: number
  message?: string
  result?: T
}

// ========== AUTH / USER ==========
export interface LoginRequest {
  name: string
  password: string
}

export interface RegisterRequest {
  name: string
  email: string
  phone: string
  password: string
  password_confirmation?: string
}

export interface User {
  id: number
  name: string
  email?: string
  email_verified_at?: string | null
  phone?: string
  phone_number?: string | null
  full_name?: string
  status?: 'active' | 'inactive' | string
  role_name?: string | null
  role_id?: number | null
  authorities?: string[]
  created_at?: string
  updated_at?: string
}

export interface AuthResponse {
  token: string
  isAuthenticated?: boolean
}

export interface AuthSession {
  token: string
  user: User
  authorities: string[]
  isAuthenticated: boolean
}

// ========== ROLE / PERMISSION ==========
export interface PermissionResponse {
  id?: number
  name: string
  description?: string | null
  Id?: number
  Name?: string
  Description?: string | null
}

export interface RoleResponse {
  id?: number
  name: string
  description?: string | null
  permissions?: PermissionResponse[]
  Id?: number
  Name?: string
  Description?: string | null
}

// ========== CATEGORY ==========
export interface CategoryResponse {
  id: number
  name: string
  slug: string
  created_at?: string
  updated_at?: string
  Id?: number
  Name?: string
  Slug?: string
}

// ========== PRODUCT ==========
export interface ProductResponse {
  id: number
  name: string
  slug: string
  description: string
  isVaulted?: boolean
  categoryId?: number | null
  categoryName?: string | null
  created_at?: string
  updated_at?: string
  Id?: number
  Name?: string
  Slug?: string
  Description?: string
  IsVaulted?: boolean
  category_id?: number | null
  CategoryId?: number | null
  category_name?: string | null
  CategoryName?: string | null
  Status?: string
  Category?: {
    Id?: number
    id?: number
    Name?: string
    name?: string
    slug?: string
    Slug?: string
  }
  Supplier?: {
    Id?: number
    id?: number
    Name?: string
    name?: string
  }
  Variants?: Array<{
    Id?: number
    id?: number
    Name?: string
    name?: string
    Sku?: string
    sku?: string
    Price?: number
    price?: number
    OriginalPrice?: number | null
    original_price?: number | null
    ImageUrl?: string | null
    image_url?: string | null
  }>
  Images?: string[]
  Rating?: number
  ReviewCount?: number
  SoldCount?: number
}

export type Product = ProductResponse
export type ProductDetail = ProductResponse

// ========== ORDER ==========
export interface OrderResponse {
  id: number
  totalAmount: number
  status: string
  createdAt?: string
  userId?: number | null
  username?: string | null
  Id?: number
  total_amount?: number
  status_name?: string
  created_at?: string
  user_id?: number | null
  UserId?: number | null
  username_name?: string | null
  items?: Array<Record<string, unknown>>
  payment?: Record<string, unknown>
  branch?: Record<string, unknown>
}

export interface OrderStatusRequest {
  status: string
}

// ========== BACKWARD-COMPATIBLE SUPPORT TYPES ==========
export interface Supplier {
  id: number
  name: string
  contact_info?: string | null
}

export interface Branch {
  id: number
  name: string
  location?: string | null
}

export interface Address {
  id: number
  user_id: number
  recipient_name: string
  recipient_phone: string
  street_address: string
  ward: string
  district: string
  city: string
  is_default: boolean
}

export interface CartItem {
  id: number
  user_id: number
  product_variant_id: number
  quantity: number
  price: number
}

export interface CartItemLocal {
  product_variant_id: number
  quantity: number
  price: number
}

export interface ProductVariant {
  id: number
  name: string
  sku: string
  price: number
  original_price?: number | null
  image_url?: string | null
}

export type OrderStatus = 'pending' | 'processing' | 'shipped' | 'delivered' | 'cancelled'
