<template>
  <v-container class="py-10">
    <v-card class="surface-card pa-8 text-center">
      <v-icon size="64" color="primary" class="mb-4">mdi-cart-off</v-icon>
      <h1 class="text-h4 font-weight-black mb-3">Giỏ hàng chưa được backend hỗ trợ</h1>
      <p class="muted-copy mb-6" style="max-width: 720px; margin: 0 auto;">
        Schema hiện tại của backend chưa có API tạo giỏ hàng hoặc tạo đơn hàng mới,
        nên trang này chỉ đóng vai trò thông báo thay vì hiển thị dữ liệu giả.
      </p>
      <v-btn color="primary" to="/productlist">Quay về catalog</v-btn>
    </v-card>
  </v-container>
</template>
