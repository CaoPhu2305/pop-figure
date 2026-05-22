<template>
  <v-container class="py-10">
    <v-card class="surface-card pa-8">
      <div class="section-heading mb-2">Checkout</div>
      <h1 class="text-h4 font-weight-black mb-3">Thanh toán tạm thời</h1>
      <p class="muted-copy mb-6">
        Backend hiện chưa có endpoint tạo đơn hàng, nên màn này được giữ lại như một placeholder rõ ràng
        để không dẫn người dùng vào một luồng giả.
      </p>
      <v-alert type="info" variant="tonal" class="mb-6">
        Khi có API tạo order, mình sẽ nối form giao hàng, payment và submit ở đây.
      </v-alert>
      <v-btn color="primary" to="/productlist">Tiếp tục mua sắm</v-btn>
    </v-card>
  </v-container>
</template>
