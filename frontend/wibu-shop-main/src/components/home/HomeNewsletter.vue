<template>
  <v-container class="py-16 section-shell">
    <v-card class="cta-card rounded-xl pa-10 surface-card">
      <v-row align="center">
        <v-col cols="12" lg="7">
          <h2 class="text-h4 font-weight-bold mb-4 page-title">
            Nhận lịch phát hành & mã giảm độc quyền
          </h2>
          <p class="text-medium-emphasis">
            Dùng làm bản xem trước giao diện trước khi kết nối dữ liệu thật.
          </p>
        </v-col>
        <v-col cols="12" lg="5" class="d-flex ga-3">
          <v-text-field
            placeholder="Email của bạn"
            variant="outlined"
            hide-details
            rounded="pill"
            density="comfortable"
            class="cta-input"
          />
          <v-btn
            color="secondary"
            size="large"
            rounded="pill"
            class="flex-shrink-0"
          >
            Đăng ký
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<style scoped>
.cta-card {
  background: linear-gradient(
    135deg,
    rgba(0, 212, 255, 0.06) 0%,
    rgba(17, 22, 42, 0.94) 50%,
    rgba(255, 0, 255, 0.06) 100%
  ) !important;
  border: 1px solid rgba(148, 163, 184, 0.12) !important;
}

.cta-input :deep(.v-field) {
  background: rgba(255, 255, 255, 0.04) !important;
}
</style>
