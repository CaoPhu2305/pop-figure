<template>
  <v-app-bar class="app-header" height="82" flat>
    <v-container class="header-inner" fluid>
      <router-link to="/" class="brand-link">
        <div class="brand-mark">
          <v-icon size="20" color="white">mdi-cube</v-icon>
        </div>
        <div>
          <div class="brand-title">PopFigure</div>
          <div class="brand-subtitle">Collectible Store</div>
        </div>
      </router-link>

      <div class="nav-desktop hidden-md-and-down">
        <v-btn to="/" variant="text" class="nav-btn">Trang chủ</v-btn>
        <v-btn to="/productlist" variant="text" class="nav-btn">Sản phẩm</v-btn>
        <v-btn to="/order" variant="text" class="nav-btn">Đơn hàng</v-btn>
        <v-btn v-if="isAdmin" to="/admin" variant="text" class="nav-btn">Quản trị</v-btn>
      </div>

      <v-responsive max-width="340" class="search-shell hidden-lg-and-down">
        <v-text-field
          v-model="searchQuery"
          density="compact"
          variant="outlined"
          rounded="pill"
          hide-details
          prepend-inner-icon="mdi-magnify"
          placeholder="Tìm sản phẩm..."
          @keyup.enter="handleSearch"
        />
      </v-responsive>

      <div class="actions-wrap">
        <v-btn
          icon
          variant="text"
          class="hidden-lg-and-up"
          @click="showMobileMenu = !showMobileMenu"
        >
          <v-icon>mdi-menu</v-icon>
        </v-btn>

        <template v-if="isAuthenticated">
          <v-menu>
            <template #activator="{ props }">
              <v-btn v-bind="props" variant="tonal" rounded="pill" class="profile-btn">
                <v-avatar size="26" color="primary" class="mr-2">
                  <span class="text-caption text-white font-weight-bold">{{ userInitials }}</span>
                </v-avatar>
                <span class="hidden-sm-and-down">{{ displayName }}</span>
                <v-icon size="18" class="ml-1">mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            <v-list rounded="xl" class="surface-card" min-width="220">
              <v-list-item to="/profile" title="Thông tin cá nhân" prepend-icon="mdi-account-circle-outline" />
              <v-list-item to="/order" title="Đơn hàng của tôi" prepend-icon="mdi-receipt-text-outline" />
              <v-list-item v-if="isAdmin" to="/admin" title="Khu vực quản trị" prepend-icon="mdi-shield-crown-outline" />
              <v-divider class="my-1" />
              <v-list-item title="Đăng xuất" prepend-icon="mdi-logout" @click="handleLogout" />
            </v-list>
          </v-menu>
        </template>

        <v-btn v-else to="/login" color="primary" rounded="pill" class="login-btn">
          Đăng nhập
        </v-btn>
      </div>
    </v-container>
  </v-app-bar>

  <v-navigation-drawer v-model="showMobileMenu" location="right" temporary class="mobile-drawer">
    <div class="pa-4">
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        placeholder="Tìm theo tên, slug"
        variant="outlined"
        rounded="lg"
        density="compact"
        hide-details
        class="mb-4"
        @keyup.enter="handleSearch"
      />

      <v-list nav>
        <v-list-item to="/" title="Trang chủ" prepend-icon="mdi-home-variant-outline" />
        <v-list-item to="/productlist" title="Sản phẩm" prepend-icon="mdi-view-grid-outline" />
        <v-list-item to="/order" title="Đơn hàng" prepend-icon="mdi-receipt-text-outline" />
        <v-list-item v-if="isAdmin" to="/admin" title="Quản trị" prepend-icon="mdi-shield-crown-outline" />
        <v-divider class="my-3" />
        <v-list-subheader>Danh mục</v-list-subheader>
        <v-list-item
          v-for="category in categoryLinks"
          :key="category.id"
          :to="{ path: '/productlist', query: { category: category.id } }"
          :title="category.name"
          :subtitle="category.slug"
        />
      </v-list>
    </div>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import { useAuthStore } from '@/stores/auth.store'
import { useCategoriesStore } from '@/stores/categories.store'

const router = useRouter()
const authStore = useAuthStore()
const categoriesStore = useCategoriesStore()

const showMobileMenu = ref(false)
const searchQuery = ref('')

const isAuthenticated = computed(() => authStore.isAuthenticated)
const isAdmin = computed(() => authStore.isAdmin)
const displayName = computed(() => authStore.displayName)
const userInitials = computed(() => authStore.initials)
const categoryLinks = computed(() => categoriesStore.categories.slice(0, 10))

const handleSearch = () => {
  const keyword = searchQuery.value.trim()
  router.push({
    path: '/productlist',
    query: keyword ? { search: keyword } : {},
  })
  showMobileMenu.value = false
}

const handleLogout = async () => {
  await authStore.logout()
  router.push('/login')
}

onMounted(async () => {
  await authStore.initialize()
  await categoriesStore.fetchCategories()
})
</script>

<style scoped>
.app-header {
  background: rgba(255, 250, 243, 0.94) !important;
  border-bottom: 1px solid rgba(92, 66, 48, 0.14) !important;
  backdrop-filter: blur(14px);
}

.header-inner {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
  color: inherit;
  text-decoration: none;
}

.brand-mark {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #c44f2d 0%, #356d6b 100%);
}

.brand-title {
  font-size: 1.05rem;
  font-weight: 800;
  line-height: 1.1;
}

.brand-subtitle {
  font-size: 0.72rem;
  color: rgba(75, 63, 53, 0.74);
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.nav-desktop {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-left: 6px;
}

.nav-btn {
  font-weight: 600;
}

.search-shell {
  margin-left: auto;
  margin-right: 6px;
}

.actions-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile-btn {
  max-width: 230px;
}

.login-btn {
  font-weight: 700;
}

.mobile-drawer {
  background: rgba(255, 250, 243, 0.98);
}
</style>
