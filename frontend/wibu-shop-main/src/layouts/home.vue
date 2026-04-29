<template>
  <div class="app-layout">
    <AppHeader />

    <v-main class="main-content">
      <router-view />
    </v-main>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue';
import AppFooter from '@/components/AppFooter.vue';
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  background:
    radial-gradient(circle at top left, rgba(0, 212, 255, 0.05), transparent 30%),
    linear-gradient(180deg, #0b1020 0%, #10172c 100%);
}
</style>