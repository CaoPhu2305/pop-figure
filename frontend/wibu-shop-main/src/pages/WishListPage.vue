<template>
  <v-container class="py-10">
    <v-card class="surface-card pa-8 text-center">
      <v-icon size="64" color="secondary" class="mb-4">mdi-heart-off-outline</v-icon>
      <h1 class="text-h4 font-weight-black mb-3">Wishlist không còn là dữ liệu lõi</h1>
      <p class="muted-copy mb-6" style="max-width: 720px; margin: 0 auto;">
        Vì backend hiện không có bảng hoặc API wishlist, giao diện này được giữ như một trang thông báo
        thay vì mô phỏng dữ liệu giả.
      </p>
      <v-btn color="primary" to="/productlist">Xem catalog</v-btn>
    </v-card>
  </v-container>
</template>
