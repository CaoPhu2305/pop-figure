<template>
  <v-app-bar class="app-header" height="84" flat>
    <v-container class="header-inner" fluid>
      <router-link to="/" class="brand-link">
        <div class="brand-mark">
          <v-icon size="20" color="white">mdi-cube-outline</v-icon>
        </div>
        <div>
          <div class="brand-title">PopFigure</div>
          <div class="brand-subtitle">Anime & Collectibles</div>
        </div>
      </router-link>

      <div class="nav-desktop hidden-md-and-down">
        <v-btn to="/" variant="text" class="nav-btn">Trang chủ</v-btn>
        <v-btn to="/products" variant="text" class="nav-btn">Sản phẩm</v-btn>
      </div>

      <v-responsive max-width="380" class="search-shell hidden-sm-and-down">
        <v-text-field
          v-model="searchQuery"
          density="compact"
          variant="outlined"
          rounded="pill"
          hide-details
          prepend-inner-icon="mdi-magnify"
          placeholder="Tìm kiếm mô hình, nhân vật..."
          @keyup.enter="handleSearch"
        />
      </v-responsive>

      <div class="actions-wrap">
        <v-btn color="primary" rounded="pill" class="hidden-md-and-down" @click="handleSearch">
          Khám phá ngay
        </v-btn>

        <v-btn icon variant="text" class="hidden-lg-and-up" @click="showMobileMenu = !showMobileMenu">
          <v-icon>mdi-menu</v-icon>
        </v-btn>
      </div>
    </v-container>
  </v-app-bar>

  <v-navigation-drawer v-model="showMobileMenu" location="right" temporary class="mobile-drawer">
    <div class="pa-4">
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        placeholder="Tìm theo tên hoặc slug"
        variant="outlined"
        rounded="lg"
        density="compact"
        hide-details
        class="mb-4"
        @keyup.enter="handleSearch"
      />

      <v-list nav>
        <v-list-item to="/" title="Trang chủ" prepend-icon="mdi-home-variant-outline" />
        <v-list-item to="/products" title="Danh sách sản phẩm" prepend-icon="mdi-view-grid-outline" />
        <v-divider class="my-3" />
        <v-list-subheader>Danh mục nổi bật</v-list-subheader>
        <v-list-item
          v-for="category in categoryLinks"
          :key="category.id"
          :to="{ path: '/products', query: { category: category.id } }"
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

import { useCategoriesStore } from '@/stores/categories.store'

const router = useRouter()
const categoriesStore = useCategoriesStore()

const showMobileMenu = ref(false)
const searchQuery = ref('')

const categoryLinks = computed(() => categoriesStore.categories.slice(0, 10))

const handleSearch = () => {
  const keyword = searchQuery.value.trim()
  router.push({
    path: '/products',
    query: keyword ? { search: keyword } : {},
  })
  showMobileMenu.value = false
}

onMounted(async () => {
  await categoriesStore.fetchCategories()
})
</script>

<style scoped>
.app-header {
  background: rgba(255, 250, 243, 0.95) !important;
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
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #c44f2d 0%, #356d6b 100%);
}

.brand-title {
  font-size: 1.08rem;
  font-weight: 800;
  line-height: 1.1;
}

.brand-subtitle {
  font-size: 0.72rem;
  color: rgba(75, 63, 53, 0.74);
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.nav-desktop {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-left: 8px;
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

.mobile-drawer {
  background: rgba(255, 250, 243, 0.98);
}
</style>
