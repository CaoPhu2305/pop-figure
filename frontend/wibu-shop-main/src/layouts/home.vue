<template>
  <div class="app-shell">
    <AppHeader />

    <v-main class="main-content">
      <router-view />
    </v-main>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
}

.main-content {
  position: relative;
}

.main-content::before {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background:
    radial-gradient(circle at top left, rgba(196, 79, 45, 0.04), transparent 25%),
    radial-gradient(circle at bottom right, rgba(53, 109, 107, 0.04), transparent 30%);
}
</style>
