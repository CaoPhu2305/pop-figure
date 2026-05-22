<template>
  <v-container class="py-16 section-shell">
    <div class="text-center mb-12">
      <div class="text-overline text-primary tracking-widest mb-2">KHÁM PHÁ</div>
      <h2 class="text-h4 font-weight-bold page-title">Mua sắm theo vibes</h2>
    </div>

    <v-row>
      <v-col v-for="cat in categories" :key="cat.name" cols="12" sm="6" lg="3">
        <v-hover v-slot:default="{ isHovering, props }">
          <v-card
            v-bind="props"
            class="category-card rounded-xl overflow-hidden cursor-pointer position-relative surface-card"
            height="300"
          >
            <v-img
              :src="cat.image"
              cover
              class="fill-height transition-transform"
              :class="{ 'scale-up': isHovering }"
            />
            <div class="category-overlay d-flex flex-column justify-space-between pa-6">
              <div>
                <div class="text-caption" style="color: rgba(255,255,255,0.7);">{{ cat.count }} sản phẩm</div>
                <h3 class="text-h5 font-weight-bold text-white mt-2">{{ cat.name }}</h3>
              </div>
              <v-btn
                variant="text"
                color="white"
                class="px-0 category-link"
                append-icon="mdi-chevron-right"
                :to="{ path: '/productlist', query: { category: cat.slug } }"
              >
                Khám phá
              </v-btn>
            </div>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { categories } from '@/data/mockHomePage';
</script>

<style scoped>
.category-card {
  border: 1px solid rgba(148, 163, 184, 0.12);
  transition: all 0.3s ease;
}

.category-card:hover {
  border-color: rgba(0, 212, 255, 0.22);
  box-shadow: 0 18px 28px rgba(2, 6, 23, 0.18);
}

.category-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0.2) 50%, transparent 100%);
  z-index: 2;
}

.category-link:hover {
  color: #00d4ff !important;
}

.transition-transform {
  transition: transform 0.6s ease;
}

.scale-up {
  transform: scale(1.1);
}
</style>
