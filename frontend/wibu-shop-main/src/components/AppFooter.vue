<template>
  <v-footer class="app-footer pa-0">
    <v-container class="py-10">
      <v-row class="mb-8" align="start">
        <v-col cols="12" md="5">
          <div class="d-flex align-center ga-4 mb-4">
            <v-avatar color="primary" size="48" rounded="lg">
              <v-icon color="white">mdi-cube-scan</v-icon>
            </v-avatar>
            <div>
              <div class="text-overline muted-copy">PopFigure</div>
              <div class="text-h6 font-weight-bold">Catalog and operations hub</div>
            </div>
          </div>
          <p class="muted-copy mb-0" style="max-width: 480px;">
            Giao diện này được thiết kế lại để bám sát dữ liệu thật trong database:
            sản phẩm, danh mục, đơn hàng, người dùng, vai trò và quyền truy cập.
          </p>
        </v-col>

        <v-col cols="12" md="7" class="d-flex justify-md-end">
          <div class="d-flex flex-wrap ga-2 footer-links justify-md-end">
            <v-chip to="/" variant="tonal" color="primary" size="small">Trang chủ</v-chip>
            <v-chip to="/productlist" variant="tonal" color="primary" size="small">Catalog</v-chip>
            <v-chip to="/order" variant="tonal" color="primary" size="small">Đơn hàng</v-chip>
            <v-chip to="/profile" variant="tonal" color="primary" size="small">Hồ sơ</v-chip>
          </div>
        </v-col>
      </v-row>

      <v-divider class="mb-6" />

      <div class="d-flex flex-column flex-md-row justify-space-between align-center text-caption muted-copy gap-2">
        <p class="mb-0">© 2026 PopFigure. UI rebuilt for the current backend schema.</p>
        <div class="d-flex ga-4">
          <span>Backend: Spring Boot</span>
          <span>Frontend: Vue 3 + Vuetify</span>
        </div>
      </div>
    </v-container>
  </v-footer>
</template>

<style scoped>
.app-footer {
  background: rgba(255, 250, 243, 0.92) !important;
  border-top: 1px solid rgba(92, 66, 48, 0.12);
}
</style>
