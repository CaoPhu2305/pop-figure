<template>
  <section class="flash-sale-section py-16 overflow-hidden section-shell">
    <v-container>
      <div class="text-center mb-12">
        <v-chip color="error" variant="flat" class="mb-4 animate-pulse">
          <v-icon start>mdi-lightning-bolt</v-icon>
          FLASH SALE
        </v-chip>
        <h2 class="text-h3 font-weight-bold text-white page-title">
          Ưu đãi giới hạn 72h
        </h2>
      </div>

      <v-row>
        <v-col v-for="deal in flashDeals" :key="deal.id" cols="12" md="4">
          <v-card class="deal-card rounded-xl pa-5 surface-card">
            <div class="d-flex justify-space-between mb-4">
              <span class="d-flex align-center text-caption">
                <v-icon icon="mdi-fire" color="error" class="mr-1" />
                HOT
              </span>
              <v-chip color="error" size="x-small">{{ deal.discount }}</v-chip>
            </div>
            <v-img
              :src="deal.image"
              class="rounded-lg mb-4"
              cover
              height="180"
            />
            <h3 class="text-h6 font-weight-bold text-truncate mb-2">
              {{ deal.name }}
            </h3>
            <div class="d-flex align-center ga-2 mb-4">
              <span class="text-h6 font-weight-bold neon-text-secondary">{{
                deal.salePrice
              }}</span>
              <span
                class="text-caption text-decoration-line-through text-medium-emphasis"
                >{{ deal.originalPrice }}</span
              >
            </div>
            <v-btn block color="primary" variant="flat" rounded="lg"
              >Mua ngay</v-btn
            >
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </section>
</template>

<script setup lang="ts">
import { flashDeals } from "@/data/mockHomePage";
</script>

<style scoped>
.flash-sale-section {
  background: linear-gradient(
    135deg,
    rgba(255, 0, 255, 0.08) 0%,
    rgba(0, 212, 255, 0.06) 50%,
    rgba(168, 85, 247, 0.08) 100%
  );
  position: relative;
}

.flash-sale-section::before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: radial-gradient(
    circle at 2px 2px,
    rgba(255, 255, 255, 0.05) 1px,
    transparent 0
  );
  background-size: 30px 30px;
}

.deal-card {
  backdrop-filter: blur(12px);
  transition: all 0.3s ease;
}

.deal-card:hover {
  border-color: rgba(255, 0, 255, 0.2) !important;
  box-shadow: 0 18px 28px rgba(2, 6, 23, 0.18);
}

.animate-pulse {
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.neon-text-secondary {
  color: #ff00ff;
  text-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}
</style>
