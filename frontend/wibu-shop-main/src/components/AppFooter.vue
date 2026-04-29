<template>
  <v-footer class="app-footer pa-0">
    <v-container class="py-10">
      <v-row align="center" class="mb-6">
        <v-col cols="12" md="6">
          <div class="d-flex align-center ga-4">
            <v-avatar size="48" class="logo-avatar rounded-lg">WS</v-avatar>
            <div>
              <div class="text-overline text-medium-emphasis">Wibu Shop</div>
              <div class="text-h6 font-weight-bold">Giao diện xem trước trước khi nối backend</div>
            </div>
          </div>
        </v-col>
        <v-col cols="12" md="6" class="d-flex justify-md-end">
          <div class="d-flex flex-wrap ga-2 justify-md-end footer-links">
            <v-chip to="/productlist" variant="tonal" color="primary" size="small">Sản phẩm</v-chip>
            <v-chip to="/wishlist" variant="tonal" color="primary" size="small">Yêu thích</v-chip>
            <v-chip to="/cart" variant="tonal" color="primary" size="small">Giỏ hàng</v-chip>
            <v-chip to="/profile" variant="tonal" color="primary" size="small">Tài khoản</v-chip>
          </div>
        </v-col>
      </v-row>

      <v-divider class="mb-6" />

      <div class="d-flex flex-column flex-md-row justify-space-between align-center text-caption text-medium-emphasis gap-2">
        <p>© 2026 Wibu Shop. Frontend-only mode.</p>
        <div class="d-flex ga-4">
          <a href="mailto:hello@wibushop.vn" class="link-item">hello@wibushop.vn</a>
          <a href="tel:+84123456789" class="link-item">+84 123 456 789</a>
        </div>
      </div>
    </v-container>
  </v-footer>
</template>

<style scoped>
.app-footer {
  background: rgba(11, 16, 32, 0.98) !important;
  border-top: 1px solid rgba(148, 163, 184, 0.12);
}

.logo-avatar {
  background: linear-gradient(135deg, #00d4ff 0%, #ff00ff 100%);
  color: #000;
}

.link-item {
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
}

.link-item:hover {
  color: #00d4ff !important;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.social-btn {
  transition: all 0.3s ease;
}
</style>