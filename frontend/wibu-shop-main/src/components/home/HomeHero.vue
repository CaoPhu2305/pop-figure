<template>
  <section class="hero-section py-16 position-relative overflow-hidden">
    <div class="hero-bg" />
    <div class="hero-particles" />
    
    <v-container class="position-relative" style="z-index: 2;">
      <v-row align="center">
        <v-col cols="12" lg="6" class="pr-lg-12">
          <v-chip
            prepend-icon="mdi-sparkles"
            color="primary"
            variant="flat"
            class="mb-6 animate-neon-pulse"
            rounded="pill"
          >
            Mở bản xem trước giao diện
          </v-chip>

          <h1 class="text-h3 text-md-h2 font-weight-bold mb-6">
            Chắp cánh cho
            <span class="gradient-text d-block">bản xem trước gọn gàng hơn</span>
          </h1>

          <p class="text-body-1 text-medium-emphasis mb-8" style="max-width: 500px; line-height: 1.8;">
            Giao diện được làm lại theo hướng sạch, dễ nhìn và đủ rõ để bạn kiểm tra tình hình trước khi nối backend.
          </p>

          <div class="d-flex flex-wrap ga-4 mb-10">
            <v-btn
              to="/productlist"
              color="primary"
              size="large"
              rounded="pill"
              class="neon-btn-primary"
              append-icon="mdi-chevron-right"
            >
              Xem sản phẩm
            </v-btn>
            <v-btn variant="outlined" size="large" rounded="pill" class="neon-btn-outline">
              Xem bố cục
            </v-btn>
          </div>

          <!-- Stats -->
          <v-row dense>
            <v-col v-for="stat in stats" :key="stat.label" cols="4">
              <v-card variant="flat" class="stat-card text-center py-4 rounded-xl surface-card">
                <div class="text-h5 font-weight-bold neon-text-primary">{{ stat.value }}</div>
                <div class="text-caption text-medium-emphasis">{{ stat.label }}</div>
              </v-card>
            </v-col>
          </v-row>
        </v-col>

        <v-col cols="12" lg="5">
          <v-card class="featured-card pa-5 rounded-xl surface-card">
            <div class="d-flex justify-space-between align-center mb-3">
              <span class="text-overline text-medium-emphasis">Featured preview</span>
              <v-chip size="x-small" color="success" prepend-icon="mdi-shield-check">Mock data</v-chip>
            </div>
            <v-img
              src="https://images.unsplash.com/photo-1618336753974-aae8e04506aa?q=80&w=1000"
              :aspect-ratio="4/3"
              cover
              class="rounded-xl mb-4"
              max-height="320"
            >
              <v-chip size="small" class="ma-3 bg-surface font-weight-bold trending-chip">
                <v-icon start size="small">mdi-fire</v-icon>
                Trending
              </v-chip>
            </v-img>
            <div class="d-flex justify-space-between align-end">
              <div>
                <div class="text-overline text-medium-emphasis">Nội dung mẫu</div>
                <h3 class="text-h5 font-weight-bold">Preview Collection</h3>
              </div>
              <div class="text-right">
                <div class="text-caption text-medium-emphasis">Giá từ</div>
                <div class="text-h5 font-weight-bold neon-text-secondary">1.990.000₫</div>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </section>
</template>

<script setup lang="ts">
import { stats } from '@/data/mockHomePage';
</script>

<style scoped>
.hero-section {
  min-height: 700px;
  position: relative;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at top left, rgba(0, 212, 255, 0.08) 0%, transparent 50%),
    radial-gradient(ellipse at bottom right, rgba(255, 0, 255, 0.08) 0%, transparent 50%);
  z-index: 0;
}

.hero-particles {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle at 2px 2px, rgba(0, 212, 255, 0.15) 1px, transparent 0);
  background-size: 40px 40px;
  opacity: 0.25;
  z-index: 1;
}

.gradient-text {
  background: linear-gradient(90deg, #00d4ff 0%, #ff00ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.neon-text-primary {
  color: #00d4ff;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.neon-text-secondary {
  color: #ff00ff;
  text-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}

.stat-card {
  background: rgba(255, 255, 255, 0.03) !important;
  border: 1px solid rgba(148, 163, 184, 0.12) !important;
}

.featured-card {
  backdrop-filter: blur(12px);
}

.trending-chip {
  border: 1px solid rgba(255, 0, 255, 0.3);
}

.neon-btn-primary {
  box-shadow: 0 10px 30px rgba(0, 212, 255, 0.12);
  transition: all 0.3s ease;
}

.neon-btn-primary:hover {
  box-shadow: 0 14px 30px rgba(0, 212, 255, 0.18);
}

.neon-btn-outline {
  border-color: rgba(255, 255, 255, 0.3) !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

.neon-btn-outline:hover {
  border-color: rgba(0, 212, 255, 0.6) !important;
  background: rgba(0, 212, 255, 0.1) !important;
}

.animate-neon-pulse {
  animation: neonPulse 2s ease-in-out infinite;
}

@keyframes neonPulse {
  0%, 100% { box-shadow: 0 0 10px rgba(0, 212, 255, 0.3); }
  50% { box-shadow: 0 0 25px rgba(0, 212, 255, 0.5); }
}
</style>
