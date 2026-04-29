<template>
  <v-app-bar flat class="app-header px-md-4" height="72">
    <!-- Logo -->
    <v-btn to="/" variant="text" class="pa-0 h-auto" :ripple="false">
      <div class="logo-container d-flex align-center justify-center px-4 py-2 rounded-xl">
        <span class="text-h5 font-weight-bold neon-text-primary">Wibu</span>
        <span class="text-h5 font-weight-black neon-text-secondary ml-1">Shop</span>
      </div>
    </v-btn>

    <v-spacer />

    <!-- Desktop Navigation -->
    <div class="hidden-sm-and-down d-flex align-center ga-2">
      <v-btn
        to="/"
        variant="text"
        class="nav-link"
        prepend-icon="mdi-home-variant-outline"
      >
        Home
      </v-btn>

      <v-btn
        to="/productlist"
        variant="text"
        class="nav-link"
        prepend-icon="mdi-store-outline"
      >
        Products
      </v-btn>

      <v-menu open-on-hover transition="slide-y-transition">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            variant="text"
            class="nav-link"
            append-icon="mdi-chevron-down"
          >
            Danh mục
          </v-btn>
        </template>
        <v-list class="neon-dropdown" rounded="lg" elevation="16">
          <v-list-item
            v-for="category in categoryQuickLinks"
            :key="category.to"
            :to="category.to"
            :title="category.label"
            class="dropdown-item"
          />
        </v-list>
      </v-menu>
    </div>

    <!-- Search Box -->
    <v-responsive max-width="360" class="mx-4 hidden-sm-and-down">
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        placeholder="Tìm kiếm figure anime..."
        variant="outlined"
        hide-details
        rounded="xl"
        density="compact"
        class="search-field"
        bg-color="surface-variant"
        @keyup.enter="handleSearch"
      />
    </v-responsive>

    <v-spacer />

    <!-- Action Buttons -->
    <div class="d-flex align-center ga-1">
      <!-- Notifications -->
      <v-btn v-if="isAuthenticated" icon variant="text" class="action-btn" @click="toggleNotifications">
        <v-badge :content="userUnreadCount" color="secondary" :model-value="userUnreadCount > 0">
          <v-icon>mdi-bell-outline</v-icon>
        </v-badge>
      </v-btn>

      <!-- Wishlist -->
      <v-btn icon :to="{ name: 'WishList', query: { id: 1 } }" variant="text" class="action-btn">
        <v-badge :content="wishlistCount" color="secondary" :model-value="wishlistCount > 0">
          <v-icon>mdi-heart-outline</v-icon>
        </v-badge>
      </v-btn>

      <!-- Cart -->
      <v-btn icon :to="{ name: 'Cart', query: { id: 1 } }" variant="text" class="action-btn">
        <v-badge :content="cartCount" color="secondary" :model-value="cartCount > 0">
          <v-icon>mdi-shopping-outline</v-icon>
        </v-badge>
      </v-btn>

      <!-- User Menu -->
      <v-menu v-if="isAuthenticated">
        <template v-slot:activator="{ props }">
          <v-btn v-bind="props" class="ml-2 user-btn" variant="outlined" rounded="xl">
            <v-avatar size="24" color="primary" class="mr-2">
              <span class="text-caption text-black font-weight-bold">{{ userInitials }}</span>
            </v-avatar>
            <span class="hidden-sm-and-down mr-1">{{ displayName }}</span>
            <v-icon size="small">mdi-chevron-down</v-icon>
          </v-btn>
        </template>
        <v-list class="neon-dropdown" rounded="lg" min-width="180" elevation="10">
          <v-list-item to="/profile" title="Hồ sơ" prepend-icon="mdi-account-circle" class="dropdown-item" />
          <v-list-item to="/order" title="Đơn hàng" prepend-icon="mdi-package-variant" class="dropdown-item" />
          <v-divider class="my-1" />
          <v-list-item @click="handleLogout" title="Đăng xuất" prepend-icon="mdi-logout" class="dropdown-item text-error" />
        </v-list>
      </v-menu>

      <v-btn v-else to="/login" variant="flat" color="primary" class="ml-2" rounded="xl" size="small">
        Đăng nhập
      </v-btn>

      <!-- Mobile Menu Toggle -->
      <v-app-bar-nav-icon class="hidden-md-and-up" @click="showMobileMenu = !showMobileMenu" />
    </div>
  </v-app-bar>

  <!-- Mobile Navigation Drawer -->
  <v-navigation-drawer v-model="showMobileMenu" location="right" temporary class="mobile-drawer">
    <v-list class="pa-4">
      <v-list-item to="/" title="Trang chủ" prepend-icon="mdi-home" class="dropdown-item mb-2" />
      <v-list-item :to="{ name: 'ProductList' }" title="Sản phẩm" prepend-icon="mdi-store" class="dropdown-item mb-2" />
      <v-list-item :to="{ name: 'WishList' }" title="Yêu thích" prepend-icon="mdi-heart" class="dropdown-item mb-2" />
      <v-list-item :to="{ name: 'Cart' }" title="Giỏ hàng" prepend-icon="mdi-cart" class="dropdown-item mb-2" />
      <v-divider class="my-4" />
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        placeholder="Tìm kiếm..."
        variant="outlined"
        rounded="lg"
        density="compact"
        hide-details
        class="search-field"
        @keyup.enter="handleSearch"
      />
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth.store';

const router = useRouter();
const authStore = useAuthStore();

// Computed từ auth store
const isAuthenticated = computed(() => authStore.isAuthenticated);
const currentUser = computed(() => authStore.user);

// Lấy chữ cái đầu để hiển thị avatar
const userInitials = computed(() => {
  if (!currentUser.value?.full_name) return 'U';
  return currentUser.value.full_name
    .split(' ')
    .map(n => n[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();
});

// Tên hiển thị
const displayName = computed(() => {
  return currentUser.value?.full_name || currentUser.value?.name || 'User';
});

// Mock data cho cart/wishlist (bạn sẽ config sau)
const cartCount = ref(0);
const wishlistCount = ref(0);
const userUnreadCount = ref(0);

const searchQuery = ref('');
const showMobileMenu = ref(false);

const categoryQuickLinks = [
  { label: '🎭 Figures', to: '/productlist?category=figures' },
  { label: '🐣 Nendoroids', to: '/productlist?category=nendoroids' },
  { label: '🧸 Plushies', to: '/productlist?category=plushies' },
  { label: '🤖 Figma', to: '/productlist?category=figma' },
];

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/productlist', query: { search: searchQuery.value } });
  }
};

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

const toggleNotifications = () => {
  alert('Mở danh sách thông báo');
};

// Khởi tạo auth store khi component mount
onMounted(() => {
  authStore.initialize();
});
</script>

<style scoped>
.app-header {
  background: rgba(11, 16, 32, 0.92) !important;
  backdrop-filter: blur(18px);
  border-bottom: 1px solid rgba(148, 163, 184, 0.12) !important;
}

.logo-container {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(148, 163, 184, 0.14);
  transition: all 0.3s ease;
}

.logo-container:hover {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: 0 10px 30px rgba(2, 6, 23, 0.25);
}

.nav-link {
  color: rgba(226, 232, 240, 0.82) !important;
  transition: all 0.3s ease;
}

.nav-link:hover {
  color: #00d4ff !important;
}

.search-field :deep(.v-field) {
  background: rgba(255, 255, 255, 0.04) !important;
  border: 1px solid rgba(148, 163, 184, 0.12);
}

.search-field :deep(.v-field:focus-within) {
  border-color: rgba(0, 212, 255, 0.35);
  box-shadow: 0 0 0 1px rgba(0, 212, 255, 0.12);
}

.action-btn {
  color: rgba(226, 232, 240, 0.78) !important;
  transition: all 0.3s ease;
}

.action-btn:hover {
  color: #00d4ff !important;
}

.user-btn {
  border-color: rgba(148, 163, 184, 0.18) !important;
  color: rgba(226, 232, 240, 0.9) !important;
}

.user-btn:hover {
  border-color: rgba(0, 212, 255, 0.35) !important;
  background: rgba(0, 212, 255, 0.08) !important;
}

.neon-dropdown {
  background: rgba(17, 22, 42, 0.98) !important;
  border: 1px solid rgba(148, 163, 184, 0.12) !important;
  box-shadow: 0 12px 32px rgba(2, 6, 23, 0.35) !important;
}

.dropdown-item {
  border-radius: 8px;
  margin: 2px 8px;
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background: rgba(0, 212, 255, 0.08) !important;
}

.mobile-drawer {
  background: rgba(11, 16, 32, 0.98) !important;
}
</style>