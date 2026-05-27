<template>
  <v-layout class="admin-shell">
    <v-navigation-drawer permanent width="260" class="admin-sidebar">
      <div class="pa-5">
        <router-link to="/admin" class="brand-link">
          <div class="brand-mark"><v-icon color="white" size="18">mdi-shield-crown-outline</v-icon></div>
          <div>
            <div class="brand-title">PopFigure Admin</div>
            <div class="brand-subtitle">Management Center</div>
          </div>
        </router-link>
      </div>

      <v-list nav density="comfortable" class="px-2">
        <v-list-item to="/admin" title="Tổng quan" prepend-icon="mdi-view-dashboard-outline" />
        <v-list-item to="/admin/accounts" title="Tài khoản" prepend-icon="mdi-account-cog-outline" />
        <v-list-item to="/" title="Về trang mua sắm" prepend-icon="mdi-storefront-outline" />
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-app-bar flat class="admin-topbar">
        <v-container fluid class="d-flex align-center justify-space-between">
          <div>
            <div class="text-caption text-medium-emphasis">Hệ thống quản trị</div>
            <div class="text-subtitle-1 font-weight-bold">Phân quyền tách biệt với trang người dùng</div>
          </div>
          <v-btn color="primary" variant="tonal" to="/">Xem cửa hàng</v-btn>
        </v-container>
      </v-app-bar>
      <router-view />
    </v-main>
  </v-layout>
</template>

<style scoped>
.admin-shell {
  min-height: 100vh;
  background: #fbfaf7;
}

.admin-sidebar {
  border-right: 1px solid rgba(92, 66, 48, 0.14);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.95), rgba(255, 247, 236, 0.95));
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: inherit;
}

.brand-mark {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #7a3f2f 0%, #2d5e69 100%);
}

.brand-title {
  font-weight: 800;
  line-height: 1.1;
}

.brand-subtitle {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: rgba(75, 63, 53, 0.74);
}

.admin-topbar {
  background: rgba(255, 252, 248, 0.94) !important;
  border-bottom: 1px solid rgba(92, 66, 48, 0.14);
}
</style>
