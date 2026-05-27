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
              <div class="text-h6 font-weight-bold">Storefront for collectors</div>
            </div>
          </div>
          <p class="muted-copy mb-0" style="max-width: 480px;">
            Cửa hàng mô hình và đồ sưu tầm dành cho cộng đồng anime.
            Trải nghiệm mua sắm tập trung vào giới thiệu sản phẩm và khám phá catalog.
          </p>
        </v-col>

        <v-col cols="12" md="7" class="d-flex justify-md-end">
          <div class="d-flex flex-wrap ga-2 footer-links justify-md-end">
            <v-chip to="/" variant="tonal" color="primary" size="small">Trang chủ</v-chip>
            <v-chip to="/products" variant="tonal" color="primary" size="small">Sản phẩm</v-chip>
          </div>
        </v-col>
      </v-row>

      <v-divider class="mb-6" />

      <div class="d-flex flex-column flex-md-row justify-space-between align-center text-caption muted-copy gap-2">
        <p class="mb-0">© 2026 PopFigure. All rights reserved.</p>
        <div class="d-flex ga-4">
          <span>Curated collectibles</span>
          <span>Built with Vue 3 + Vuetify</span>
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
