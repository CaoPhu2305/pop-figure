<template>
  <v-container class="py-16 section-shell">
    <div class="d-flex justify-space-between align-end mb-10">
      <div>
        <div class="text-overline text-primary mb-1">WEEKLY PICKS</div>
        <h2 class="text-h4 font-weight-bold page-title">
          Mẫu đáng đầu tư tuần này
        </h2>
      </div>
      <v-btn variant="text" color="primary" append-icon="mdi-arrow-right"
        >Xem tất cả</v-btn
      >
    </div>

    <v-row>
      <v-col
        v-for="product in featuredProducts"
        :key="product.id"
        cols="12"
        sm="6"
        lg="3"
      >
        <v-card
          class="product-card rounded-xl pa-3 surface-card"
          variant="outlined"
        >
          <v-img
            :src="product.image"
            class="rounded-lg mb-4"
            cover
            height="240"
          >
            <v-btn icon size="small" variant="flat" class="ma-2 wishlist-btn">
              <v-icon>mdi-heart-outline</v-icon>
            </v-btn>
          </v-img>
          <div class="text-caption text-primary">{{ product.category }}</div>
          <h4 class="text-subtitle-1 font-weight-bold text-truncate">
            {{ product.name }}
          </h4>
          <div class="d-flex justify-space-between align-center mt-3">
            <span class="text-h6 font-weight-bold neon-text-secondary">{{
              product.price
            }}</span>
            <v-btn
              icon="mdi-cart-plus"
              variant="flat"
              size="small"
              color="primary"
            />
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { featuredProducts } from "@/data/mockHomePage";
</script>

<style scoped>
.product-card {
  background: rgba(255, 255, 255, 0.02) !important;
  border-color: rgba(255, 255, 255, 0.08) !important;
  transition: all 0.3s ease;
}

.product-card:hover {
  border-color: rgba(0, 212, 255, 0.4) !important;
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.wishlist-btn {
  background: rgba(0, 0, 0, 0.5) !important;
  color: white !important;
}

.wishlist-btn:hover {
  background: rgba(255, 0, 255, 0.8) !important;
}

.neon-text-secondary {
  color: #ff00ff;
  text-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}
</style>
