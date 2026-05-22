import { createRouter, createWebHistory } from 'vue-router'
import { setupLayouts } from 'virtual:generated-layouts'

import { useAuthStore } from '@/stores/auth.store'

type RouteMetaFlags = {
  requiresAuth?: boolean
  guest?: boolean
  roles?: string[]
}

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/LoginPage.vue'),
    meta: { guest: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/RegisterPage.vue'),
    meta: { guest: true },
  },
  {
    path: '/',
    name: 'HomePage',
    component: () => import('@/layouts/home.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/pages/HomePage.vue'),
      },
      {
        path: 'productlist',
        name: 'ProductList',
        component: () => import('@/pages/ProductListPage.vue'),
      },
      {
        path: 'product',
        name: 'ProductDetail',
        component: () => import('@/pages/ProductDetailPage.vue'),
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/pages/OrderPage.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/pages/ProfilePage.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'admin',
        name: 'Admin',
        component: () => import('@/pages/AdminPage.vue'),
        meta: {
          requiresAuth: true,
          roles: ['ROLE_ADMIN', 'PRODUCT_MANAGE', 'CATEGORY_MANAGE', 'ORDER_VIEW', 'ORDER_MANAGE', 'USER_VIEW'],
        },
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('@/pages/CartPage.vue'),
      },
      {
        path: 'checkout',
        name: 'Checkout',
        component: () => import('@/pages/CheckoutPage.vue'),
      },
      {
        path: 'wishlist',
        name: 'WishList',
        component: () => import('@/pages/WishListPage.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: setupLayouts(routes),
})

router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (localStorage.getItem('vuetify:dynamic-reload')) {
      console.error('Dynamic import error, reloading page did not fix it', err)
    } else {
      console.log('Reloading page to fix dynamic import error')
      localStorage.setItem('vuetify:dynamic-reload', 'true')
      location.assign(to.fullPath)
    }
  } else {
    console.error(err)
  }
})

router.beforeEach(async (to) => {
  const authStore = useAuthStore()

  if (!authStore.isInitialized) {
    await authStore.initialize()
  }

  const meta = to.meta as RouteMetaFlags
  const requiresAuth = Boolean(meta.requiresAuth)
  const guestOnly = Boolean(meta.guest)
  const requiredScopes = Array.isArray(meta.roles) ? meta.roles : []

  if (requiresAuth && !authStore.isAuthenticated) {
    return {
      path: '/login',
      query: { redirect: to.fullPath },
    }
  }

  if (guestOnly && authStore.isAuthenticated) {
    return { path: '/' }
  }

  if (requiredScopes.length > 0) {
    const hasAccess = requiredScopes.some((scope) =>
      scope.startsWith('ROLE_')
        ? authStore.hasRole(scope.replace('ROLE_', ''))
        : authStore.hasAuthority(scope),
    )

    if (!hasAccess) {
      return { path: '/', query: { denied: 'role' } }
    }
  }

  return true
})

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload')
})

export default router
