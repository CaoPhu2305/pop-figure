/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router'
import { setupLayouts } from 'virtual:generated-layouts'
import { useAuthStore } from '@/stores/auth.store'

// import { routes } from 'vue-router/auto-routes'

type RouteMetaFlags = {
  requiresAuth?: boolean
  guest?: boolean
  roles?: string[]
}

const routes = [
  // Route Login - Không dùng layout (full page)
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/LoginPage.vue'),
    meta: { guest: true }
  },
  // Route Register
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/RegisterPage.vue'),
    meta: { guest: true }
  },
  // Routes chính với layout
  {
    path: '/',
    name: 'HomePage',
    component: () => import('@/layouts/home.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/pages/HomePage.vue')
      },
      {
        path: '/productlist',
        name: 'ProductList',
        component: () => import('@/pages/ProductListPage.vue')
      },
      {
        path: '/product',
        name: 'ProductDetail',
        component: () => import('@/pages/ProductDetailPage.vue')
      },
      {
        path: '/cart',
        name: 'Cart',
        component: () => import('@/pages/CartPage.vue')
      },
      {
        path: '/wishlist',
        name: 'WishList',
        component: () => import('@/pages/WishListPage.vue')
      },
      {
        path: '/checkout',
        name: 'Checkout',
        component: () => import('@/pages/CheckoutPage.vue')
        ,
        meta: { requiresAuth: true }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/pages/ProfilePage.vue')
        ,
        meta: { requiresAuth: true }
      },
      {
        path: '/order',
        name: 'Order',
        component: () => import('@/pages/OrderPage.vue')
        ,
        meta: { requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: setupLayouts(routes),
})

// Workaround for https://github.com/vitejs/vite/issues/11804
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

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (!authStore.isInitialized) {
    authStore.initialize()
  }

  const meta = to.meta as RouteMetaFlags
  const requiresAuth = Boolean(meta.requiresAuth)
  const guestOnly = Boolean(meta.guest)
  const allowedRoles = Array.isArray(meta.roles) ? meta.roles : []

  if (requiresAuth && !authStore.isAuthenticated) {
    return {
      path: '/login',
      query: { redirect: to.fullPath },
    }
  }

  if (guestOnly && authStore.isAuthenticated) {
    return { path: '/' }
  }

  if (allowedRoles.length > 0) {
    const roleName = authStore.user?.role_name?.toUpperCase()
    if (!roleName || !allowedRoles.map((role) => role.toUpperCase()).includes(roleName)) {
      return { path: '/', query: { denied: 'role' } }
    }
  }

  return true
})

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload')
})

export default router
